package com.example.EmpowerTasksAndMood

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Adapters.TasksAdapter
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Tasks
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodayTasks : AppCompatActivity() {
    private lateinit var recycleViewTodayTasks: RecyclerView
    private lateinit var todayTasksList: ArrayList<Tasks>

    val dbHelper: DBHelper = DBHelper(this)
    var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_tasks)

        userId = intent.getIntExtra("user_ID", 0)

        recycleViewTodayTasks = findViewById(R.id.recycleViewTodayTasks)
        recycleViewTodayTasks.layoutManager = LinearLayoutManager(this)

        todayTasks()
        displayTodayTasksNumber()
        deleteTasks()
    }

    fun displayTodayTasksNumber(){
        val todayFolderNumberTasks = findViewById<TextView>(R.id.textViewNumberTodayTasks)
        todayFolderNumberTasks.text = recycleViewTodayTasks.adapter?.itemCount.toString()
    }

    fun todayTasks() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val newcursor: Cursor? = dbHelper.getTasks()
        todayTasksList = ArrayList()

        while (newcursor?.moveToNext() == true) {
            val tasksID = newcursor.getInt(0)
            val userID = newcursor.getInt(1)
            val folderID = newcursor.getInt(2)
            val tasksDescription = newcursor.getString(3)
            val tasksDueDate = newcursor.getString(4)
            val tasksPriority = newcursor.getString(5)
            val tasksStatusBoolean = newcursor.getInt(6) != 0

            if (tasksDueDate == currentDate && userID == userId) {
                todayTasksList.add(
                    Tasks(tasksID, userID, folderID, tasksDescription, tasksDueDate, tasksPriority, tasksStatusBoolean)
                )
            }
        }
        newcursor?.close()
        recycleViewTodayTasks.adapter = TasksAdapter(todayTasksList, dbHelper)
    }


    private fun deleteTasks() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val taskToDelete = (recycleViewTodayTasks.adapter as TasksAdapter).tasksList[viewHolder.adapterPosition]
                (recycleViewTodayTasks.adapter as TasksAdapter).deleteItem(taskToDelete)
                displayTodayTasksNumber()
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycleViewTodayTasks)
    }

    fun menuPage(view: View){
        val intent = Intent(this, Menu::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }
}
