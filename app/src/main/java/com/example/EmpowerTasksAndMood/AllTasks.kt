package com.example.EmpowerTasksAndMood

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Adapters.TasksAdapter
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Tasks

class AllTasks : AppCompatActivity() {
    private lateinit var recycleViewAllTasks: RecyclerView
    private lateinit var allTasksList: ArrayList<Tasks>

    val dbHelper: DBHelper = DBHelper(this)
    var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tasks)

        userId = intent.getIntExtra("user_ID", 0)

        recycleViewAllTasks = findViewById(R.id.recycleViewAllTasks)
        recycleViewAllTasks.layoutManager = LinearLayoutManager(this)
        allTasks()
        displayAllTasksNumber()
        deleteTasks()
    }

    fun displayAllTasksNumber(){
        val allFolderNumberTasks = findViewById<TextView>(R.id.textViewNumberAllTasks)
        allFolderNumberTasks.text = recycleViewAllTasks.adapter?.itemCount.toString()
    }

    fun allTasks(){
        val newcursor: Cursor? = dbHelper!!.getTasks()
        allTasksList = ArrayList<Tasks>()
        while (newcursor!!.moveToNext()) {
            val tasksID = newcursor.getInt(0)
            val userID = newcursor.getInt(1)
            val folderID = newcursor.getInt(2)
            val tasksDescription = newcursor.getString(3)
            val tasksDueDate = newcursor.getString(4)
            val tasksPriority = newcursor.getString(5)
            val tasksStatusBoolean = newcursor.getInt(6) != 0
            if(userID == userId ) {
                allTasksList.add(Tasks(tasksID , userID, folderID, tasksDescription, tasksDueDate, tasksPriority, tasksStatusBoolean))
            }
        }
        recycleViewAllTasks.adapter = TasksAdapter(allTasksList, dbHelper)
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
                    val sortedTasksList = allTasksList.sortedWith(compareByDescending<Tasks> { !it.TasksStatus }.thenByDescending { it.TasksId }).toMutableList()
                    recycleViewAllTasks.adapter = TasksAdapter(sortedTasksList, dbHelper)
                    true
                }
                R.id.menu_item2 -> {
                    editor.putInt("SortPreference", 2).apply()
                    val sortedTasksList = allTasksList
                        .sortedWith(compareBy<Tasks> { it.TasksStatus }.thenBy { it.TasksDueDate })
                        .toMutableList()
                    recycleViewAllTasks.adapter = TasksAdapter(sortedTasksList, dbHelper)
                    true
                }
                R.id.menu_item3 -> {
                    editor.putInt("SortPreference", 3).apply()
                    val priorityOrder = mapOf("Priority1" to 1, "Priority2" to 2, "Priority3" to 3)
                    val sortedTasksList = allTasksList.sortedWith(compareBy<Tasks> { it.TasksStatus }.thenBy { priorityOrder[it.TasksPriority] ?: Int.MAX_VALUE }).toMutableList()
                    recycleViewAllTasks.adapter = TasksAdapter(sortedTasksList, dbHelper)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun deleteTasks() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val taskToDelete = (recycleViewAllTasks.adapter as TasksAdapter).tasksList[viewHolder.adapterPosition]
                (recycleViewAllTasks.adapter as TasksAdapter).deleteItem(taskToDelete)
                displayAllTasksNumber()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycleViewAllTasks)
    }


    fun menuPage(view:View){
        val intent = Intent(this, Menu::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }
}
