package com.example.EmpowerTasksAndMood

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Adapters.TasksAdapter
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Tasks
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TasksPage : AppCompatActivity() {
    private lateinit var recyclerViewTasks: RecyclerView
    private lateinit var newArrayList: ArrayList<Tasks>
    val dbHelper: DBHelper = DBHelper(this)

    var folderName: String = ""
    var userId: Int = 0
    var folderId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        recyclerViewTasks = findViewById(R.id.recycleViewTasks)
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        userId = intent.getIntExtra("user_ID", 0)
        folderId = intent.getIntExtra("folder_ID", 0)
        folderName = intent.getStringExtra("folder_Name") ?:  "error"

        val textViewNameFolderTasks = findViewById<TextView>(R.id.textViewNameFolderTasks)
        textViewNameFolderTasks.setText(folderName)

        displayTasks()
        displayTasksNumber()
        deleteTasks()
    }
    fun displayTasksNumber(){
        val textViewFolderNumberTasks = findViewById<TextView>(R.id.textViewFolderNumberTasks)
        textViewFolderNumberTasks.text = recyclerViewTasks.adapter?.itemCount.toString()
    }
    private fun displayTasks() {
        val sharedPrefs = getSharedPreferences("TaskPreferences", Context.MODE_PRIVATE)
        val sortPreference = sharedPrefs.getInt("SortPreference", 0)

        val newcursor: Cursor? = dbHelper!!.getTasks()
        newArrayList = ArrayList<Tasks>()
        while (newcursor!!.moveToNext()) {
            val tasksID = newcursor.getInt(0)
            val userID = newcursor.getInt(1)
            val folderID = newcursor.getInt(2)
            val tasksDescription = newcursor.getString(3)
            val tasksDueDate = newcursor.getString(4)
            val tasksPriority = newcursor.getString(5)
            val tasksStatusBoolean = newcursor.getInt(6) != 0
            if(userID == userId && folderId == folderID) {
                newArrayList.add(
                    Tasks(tasksID, userID, folderID, tasksDescription, tasksDueDate, tasksPriority, tasksStatusBoolean))
            }
        }
        val sortedTasksList = when (sortPreference) {
            1 -> newArrayList.sortedWith(compareByDescending<Tasks> { !it.TasksStatus }.thenByDescending { it.TasksId }).toMutableList()
            2 -> newArrayList.sortedWith(compareBy<Tasks> { it.TasksStatus }.thenBy { it.TasksDueDate }).toMutableList()
            3 -> { val priorityOrder = mapOf("Priority1" to 1, "Priority2" to 2, "Priority3" to 3)
                newArrayList.sortedWith(compareBy<Tasks> { it.TasksStatus }.thenBy { priorityOrder[it.TasksPriority] ?: Int.MAX_VALUE }).toMutableList()
            }
            else -> newArrayList
        }
        recyclerViewTasks.adapter = TasksAdapter(sortedTasksList, dbHelper)
    }

    fun showAddTasksDialog(view: View) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val descriptionInput = dialogView.findViewById<EditText>(R.id.inputDescription)
        val buttonDueDate = dialogView.findViewById<Button>(R.id.buttonDueDate)
        val priorityLow = dialogView.findViewById<ImageView>(R.id.priorityLow)
        val priorityMedium = dialogView.findViewById<ImageView>(R.id.priorityMedium)
        val priorityHigh = dialogView.findViewById<ImageView>(R.id.priorityHigh)

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        buttonDueDate.setOnClickListener {
            DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                buttonDueDate.text = dateFormat.format(calendar.time)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        var selectedPriority = ""

        val priorityClickListener = View.OnClickListener { priorityView ->
            priorityLow.alpha = 0.3f
            priorityMedium.alpha = 0.3f
            priorityHigh.alpha = 0.3f

            priorityView.alpha = 1.0f

            selectedPriority = when (priorityView.id) {
                R.id.priorityLow -> "Priority3"
                R.id.priorityMedium -> "Priority2"
                R.id.priorityHigh -> "Priority1"
                else -> ""
            }
        }

        priorityLow.setOnClickListener(priorityClickListener)
        priorityMedium.setOnClickListener(priorityClickListener)
        priorityHigh.setOnClickListener(priorityClickListener)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Task")
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, which ->
                val taskDescription = descriptionInput.text.toString().trim()
                val dueDate = if (buttonDueDate.text.toString() == "Select Due Date") "No due date" else buttonDueDate.text.toString().trim()

                if (taskDescription.isEmpty()) {
                    Toast.makeText(this, "Please fill in the task description", Toast.LENGTH_LONG).show()
                }
                else {
                    val newTask = Tasks(0, userId, folderId, taskDescription, dueDate, selectedPriority, false)
                    dbHelper.addTasks(newTask)
                    displayTasks()
                    displayTasksNumber()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun deleteTasks() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val taskToDelete = (recyclerViewTasks.adapter as TasksAdapter).tasksList[viewHolder.adapterPosition]
                (recyclerViewTasks.adapter as TasksAdapter).deleteItem(taskToDelete)
                displayTasksNumber()
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewTasks)
    }


    fun showPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

        val sharedPrefs = getSharedPreferences("TaskPreferences", Context.MODE_PRIVATE)

        popup.setOnMenuItemClickListener { menuItem ->
            val editor = sharedPrefs.edit()

            when (menuItem.itemId) {
                R.id.menu_item1 -> {
                    editor.putInt("SortPreference", 1).apply()
                    val sortedTasksList = newArrayList.sortedWith(compareByDescending<Tasks> { !it.TasksStatus }.thenByDescending { it.TasksId }).toMutableList()
                    recyclerViewTasks.adapter = TasksAdapter(sortedTasksList, dbHelper)
                    true
                }
                R.id.menu_item2 -> {
                    editor.putInt("SortPreference", 2).apply()
                    val sortedTasksList = newArrayList
                        .sortedWith(compareBy<Tasks> { it.TasksStatus }.thenBy { it.TasksDueDate })
                        .toMutableList()
                    recyclerViewTasks.adapter = TasksAdapter(sortedTasksList, dbHelper)
                    true
                }
                R.id.menu_item3 -> {
                    editor.putInt("SortPreference", 3).apply()
                    val priorityOrder = mapOf("Priority1" to 1, "Priority2" to 2, "Priority3" to 3)
                    val sortedTasksList = newArrayList.sortedWith(compareBy<Tasks> { it.TasksStatus }.thenBy { priorityOrder[it.TasksPriority] ?: Int.MAX_VALUE }).toMutableList()
                    recyclerViewTasks.adapter = TasksAdapter(sortedTasksList, dbHelper)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    fun mainPage(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }
}