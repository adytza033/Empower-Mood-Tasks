package com.example.EmpowerTasksAndMood.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Folder
import com.example.EmpowerTasksAndMood.R

class FolderAdapter(
    val folderList: MutableList<Folder>,
    private val dbHelper: DBHelper,
    private val userID: Int,
    private val listener: OnFolderClickListener,
    private val favoriteListener: OnFavoriteChangedListener
) :
    RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.textViewNameFolder)
        val checkBoxFavourites = itemView.findViewById<CheckBox>(R.id.checkBoxFavourites)
        val taskCount = itemView.findViewById<TextView>(R.id.textViewNumberFolderTasks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_folders, parent, false)
        return FolderViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val currentFolder = folderList[position]
        holder.name.text = currentFolder.FolderName
        holder.checkBoxFavourites.isChecked = currentFolder.isFavorite

        val taskCount = DBHelper(holder.itemView.context).getTaskCountForFolder(currentFolder.FolderID)
        holder.taskCount.text = taskCount.toString()

        holder.checkBoxFavourites.setOnClickListener {
            val isFavorite = holder.checkBoxFavourites.isChecked
            favoriteListener.onFavoriteChanged(currentFolder.FolderID, isFavorite)
        }
        holder.itemView.setOnClickListener {
            listener.onFolderClick(currentFolder.FolderID, userID, currentFolder.FolderName)
        }
    }

    interface OnFavoriteChangedListener {
        fun onFavoriteChanged(folderId: Int, isFavorite: Boolean)
    }
    interface OnFolderClickListener {
        fun onFolderClick(folderId: Int, userId: Int, folderName: String)
    }

    fun deleteFolder(folder: Folder) {
        val position = folderList.indexOf(folder)
        if (position != -1) {
            dbHelper.deleteFolder(folder.FolderID)
            folderList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}

