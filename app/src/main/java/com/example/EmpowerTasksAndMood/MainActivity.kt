package com.example.EmpowerTasksAndMood

import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Adapters.FolderAdapter
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Folder

class MainActivity : AppCompatActivity(), FolderAdapter.OnFolderClickListener, FolderAdapter.OnFavoriteChangedListener {
    private lateinit var recyclerViewFolder: RecyclerView
    private lateinit var recyclerViewFolderFavorites: RecyclerView
    private lateinit var newArrayList: ArrayList<Folder>

    val dbHelper: DBHelper = DBHelper(this)
    var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewFolder = findViewById(R.id.recycleViewProjects)
        recyclerViewFolder.layoutManager = LinearLayoutManager(this)
        recyclerViewFolder.setHasFixedSize(true)

        recyclerViewFolderFavorites = findViewById(R.id.recycleViewProjectsFavorites)
        recyclerViewFolderFavorites.layoutManager = LinearLayoutManager(this)

        userId = intent.getIntExtra("user_ID", 0)

        displayFolders()
        favoriteProjects()
        deleteFolder()

        val preferences = getSharedPreferences("com.example.EmpowerTasksAndMood.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val isFoldersVisible = preferences.getBoolean(PREF_FOLDERS_VISIBILITY, true)
        val isFoldersFavouriteVisible = preferences.getBoolean(PREF_FAVORITES_VISIBILITY, true)

        val toggleFoldersCheckBox: CheckBox = findViewById(R.id.toggleFolders)
        val toggleFoldersFavouriteCheckBox: CheckBox = findViewById(R.id.toggleFoldersFavourites)
        toggleFoldersCheckBox.isChecked = isFoldersVisible
        recyclerViewFolder.visibility = if (isFoldersVisible) View.VISIBLE else View.GONE

        toggleFoldersFavouriteCheckBox.isChecked = isFoldersFavouriteVisible
        recyclerViewFolderFavorites.visibility = if (isFoldersFavouriteVisible) View.VISIBLE else View.GONE

    }

    fun toggleFolderVisibility(view: View) {
        val checkBox = view as CheckBox
        val isFoldersVisible = checkBox.isChecked

        recyclerViewFolder.visibility = if (isFoldersVisible) View.VISIBLE else View.GONE

        val editor = getSharedPreferences("com.example.EmpowerTasksAndMood.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit()
        editor.putBoolean(PREF_FOLDERS_VISIBILITY, isFoldersVisible)
        editor.apply()
    }

    fun toggleFolderFavouriteVisibility(view: View) {
        val checkBox = view as CheckBox
        val isFoldersFavouriteVisible = checkBox.isChecked

        recyclerViewFolderFavorites.visibility = if (isFoldersFavouriteVisible) View.VISIBLE else View.GONE

        val editor = getSharedPreferences("com.example.EmpowerTasksAndMood.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit()
        editor.putBoolean(PREF_FAVORITES_VISIBILITY, isFoldersFavouriteVisible)
        editor.apply()
    }

    companion object {
        const val PREF_FOLDERS_VISIBILITY = "pref_folders_visibility"
        const val PREF_FAVORITES_VISIBILITY = "pref_favorites_visibility"
    }

    fun favoriteProjects(){
        val newcursor: Cursor? = dbHelper!!.getfolders()
        val favoriteProjects = ArrayList<Folder>()
        while(newcursor!!.moveToNext()){
            val folderID = newcursor.getInt(0)
            val userID = newcursor.getInt(1)
            val folderName = newcursor.getString(2)
            val folderType = newcursor.getString(3)
            val folderFavorite = newcursor.getInt(4) == 1
            if(userID == userId && folderFavorite == true) {
                favoriteProjects.add(Folder(folderID, userID, folderName, folderType, folderFavorite))
            }
        }
        recyclerViewFolderFavorites.adapter = FolderAdapter(favoriteProjects, dbHelper, userId, this, this)
        displayFolders()
    }

    private fun deleteFolder() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val folderToDelete = (recyclerViewFolder.adapter as FolderAdapter).folderList[position]

                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Delete Folder")
                    .setMessage("Are you sure you want to delete this folder? The action cannot be undone!")
                    .setPositiveButton("Yes") { dialog, which ->
                        (recyclerViewFolder.adapter as FolderAdapter).deleteFolder(folderToDelete)
                        dbHelper.deleteAllTasksInFolder(folderToDelete.FolderID)
                        displayFolders()
                        favoriteProjects()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        recyclerViewFolder.adapter?.notifyItemChanged(position)
                    }
                    .setOnCancelListener {
                        recyclerViewFolder.adapter?.notifyItemChanged(position)
                    }
                    .show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewFolder)
    }


    private fun displayFolders(){
        val newcursor: Cursor? = dbHelper!!.getfolders()
        newArrayList = ArrayList<Folder>()
        while(newcursor!!.moveToNext()) {
            val folderID = newcursor.getInt(0)
            val userID = newcursor.getInt(1)
            val folderName = newcursor.getString(2)
            val folderType = newcursor.getString(3)
            val folderFavorite = newcursor.getInt(4) == 1
            if (userID == userId && folderType == "Tasks") {
                newArrayList.add(Folder(folderID, userID, folderName, folderType, folderFavorite))
            }
        }
        recyclerViewFolder.adapter = FolderAdapter(newArrayList, dbHelper, userId, this, this)
    }

    override fun onFolderClick(folderId: Int, userId: Int, folderName: String) {
        val intent = Intent(this, TasksPage::class.java)
        intent.putExtra("folder_ID", folderId)
        intent.putExtra("user_ID", userId)
        intent.putExtra("folder_Name", folderName)
        startActivity(intent)
    }

    override fun onFavoriteChanged(folderId: Int, isFavorite: Boolean) {
        dbHelper.updateFolderFavoriteStatus(folderId, isFavorite)
        favoriteProjects()
    }


    fun showAddFolderDialog(view: View) {
        val context = this
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add New Folder")

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_folder, null)
        builder.setView(dialogLayout)

        val inputFolderName = dialogLayout.findViewById<EditText>(R.id.inputFolderName)
        inputFolderName.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO;

        builder.setPositiveButton("OK") { dialog, _ ->
            val folderName = inputFolderName.text.toString()
            val userId = userId
            val folderType = "Tasks"
            if(!folderName.isEmpty()) {
                val folder = Folder(1, userId, folderName, folderType, false)
                dbHelper.addFolder(folder)
                displayFolders()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    fun registerPage(view: View){
        setContentView(R.layout.activity_register)
    }

    fun menuPage(view:View){
        val intent = Intent(this, Menu::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

    fun uppdateInfo(view: View){
        setContentView(R.layout.activity_profile)
    }

    fun deleteAccount(view: View){
        setContentView(R.layout.activity_delete)
    }
}