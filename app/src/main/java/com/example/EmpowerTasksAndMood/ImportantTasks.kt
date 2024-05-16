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

class ImportantTasks : AppCompatActivity() {
    private lateinit var allImportantTasks: RecyclerView
    private lateinit var allImportantTasksList: ArrayList<Tasks>

    val dbHelper: DBHelper = DBHelper(this)
    var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_important)

        userId = intent.getIntExtra("user_ID", 0)

        allImportantTasks = findViewById(R.id.recycleViewAllImportantTaskss)
        allImportantTasks.layoutManager = LinearLayoutManager(this)
        allTasks()
        displayAllImportantTasksNumber()
        deleteTasks()
    }

    fun displayAllImportantTasksNumber(){
        val allImportantTasksNumber = findViewById<TextView>(R.id.textViewNumberAllImportantTasks)
        allImportantTasksNumber.text = allImportantTasks.adapter?.itemCount.toString()
    }

    private fun deleteTasks() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val taskToDelete = (allImportantTasks.adapter as TasksAdapter).tasksList[viewHolder.adapterPosition]
                (allImportantTasks.adapter as TasksAdapter).deleteItem(taskToDelete)
                displayAllImportantTasksNumber()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(allImportantTasks)
    }

    fun allTasks(){
        val newcursor: Cursor? = dbHelper!!.getTasks()
        allImportantTasksList = ArrayList<Tasks>()
        while (newcursor!!.moveToNext()) {
            val tasksID = newcursor.getInt(0)
            val userID = newcursor.getInt(1)
            val folderID = newcursor.getInt(2)
            val tasksDescription = newcursor.getString(3)
            val tasksDueDate = newcursor.getString(4)
            val tasksPriority = newcursor.getString(5)
            val tasksStatusBoolean = newcursor.getInt(6) != 0
            if(userID == userId && tasksPriority == "Priority1") {
                allImportantTasksList.add(
                    Tasks(tasksID, userID, folderID, tasksDescription, tasksDueDate, tasksPriority, tasksStatusBoolean))
            }
        }
        allImportantTasks.adapter = TasksAdapter(allImportantTasksList, dbHelper)
    }


    fun menuPage(view: View){
        val intent = Intent(this, Menu::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

}