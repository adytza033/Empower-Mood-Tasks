package com.example.EmpowerTasksAndMood.Model

data class Folder(
    val FolderID: Int,
    val UserID: Int,
    var FolderName: String,
    var FolderType: String,
    var isFavorite: Boolean
) {

    override fun toString(): String {
        return "folder=(FolderID = `$FolderID`, UserID = `$UserID`, Folder Name = `$FolderName`, Folder Type = `$FolderType`, Folder Favorites = `$isFavorite`)"
    }
}

