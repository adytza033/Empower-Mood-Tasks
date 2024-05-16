package com.example.EmpowerTasksAndMood.Adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Tasks
import com.example.EmpowerTasksAndMood.R

class TasksAdapter(var tasksList: MutableList<Tasks>, private val dbHelper: DBHelper) :
    RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {
    class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description = itemView.findViewById<TextView>(R.id.textViewNameTask)
        val dueDate = itemView.findViewById<TextView>(R.id.textViewTaskDate)
        val priority = itemView.findViewById<ImageView>(R.id.imageViewPriority)
        val status = itemView.findViewById<CheckBox>(R.id.taskStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_tasks, parent, false)
        return TasksViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentTasks = tasksList[position]

        holder.description.text = currentTasks.TasksDescription
        holder.dueDate.text = currentTasks.TasksDueDate
        holder.priority.setImageResource(
            when (currentTasks.TasksPriority) {
                "Priority1" -> R.drawable.baseline_flag_24
                "Priority2" -> R.drawable.baseline_flag_orange
                "Priority3" -> R.drawable.baseline_flag_yellow
                else -> R.drawable.baseline_empty
            }
        )

        holder.status.isChecked = currentTasks.TasksStatus
        holder.description.paintFlags = if (currentTasks.TasksStatus) {
            holder.description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.description.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.status.setOnClickListener {
            val isChecked = holder.status.isChecked
            currentTasks.TasksStatus = isChecked

            holder.description.paintFlags = if (isChecked) {
                holder.description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.description.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            dbHelper.updateTaskStatus(currentTasks)

            holder.itemView.post {
                reorderTasks()
            }
        }
    }

    fun reorderTasks() {
        tasksList = tasksList.sortedWith(compareByDescending<Tasks> { !it.TasksStatus }.thenByDescending { it.TasksId }).toMutableList()
        notifyDataSetChanged()
    }

    fun deleteItem(task: Tasks) {
        val position = tasksList.indexOf(task)
        if (position != -1) {
            dbHelper.deleteTask(task.TasksId)
            tasksList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}





