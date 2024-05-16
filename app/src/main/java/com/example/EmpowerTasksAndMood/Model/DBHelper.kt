
package com.example.EmpowerTasksAndMood.Model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private val DataBaseName = "EmpowerMoodAndTasks.db"
private val ver : Int = 1
class DBHelper (context: Context) : SQLiteOpenHelper(context, DataBaseName, null, ver) {

    /* User Table */
    private val UserTableName = "User"
    private val User_Column_ID = "UserId"
    private val User_Column_FullName = "UserFullName"
    private val User_Column_Email = "UserEmail"
    private val User_Column_PhoneNo = "UserPhoneNo"
    private val User_Column_UserName = "UserUserName"
    private val User_Column_Password = "UserPassword"
    private val User_Column_IsActive = "UserIsActive"

    /* Admin Table */
    private val AdminTableName = "Admin"
    private val Admin_Column_ID = "AdminId"
    private val Admin_Column_FullName = "AdminFullName"
    private val Admin_Column_Email = "AdminEmail"
    private val Admin_Column_PhoneNo = "AdminPhoneNo"
    private val Admin_Column_UserName = "AdminUserName"
    private val Admin_Column_Password = "AdminPassword"


    /* Folder Table */
    private val FolderTableName = "Folder"
    private val Folder_Column_ID = "FolderID"
    private val Folder_Column_UserID = "UserID"
    private val Folder_Column_Name = "FolderName"
    private val Folder_Column_Type = "FolderType"
    private val Folder_Column_Favorites = "FolderFavorites"

    /* Tasks Table */
    private val TasksTableName = "Tasks"
    private val Tasks_Column_ID = "TasksID"
    private val Tasks_Column_UserID = "UserID"
    private val Tasks_Column_FolderID = "FolderID"
    private val Tasks_Column_Description = "TasksDescription"
    private val Tasks_Column_DueDate = "TasksDueDate"
    private val Tasks_Column_Priority = "TasksPriority"
    private val Tasks_Column_Status = "TasksStatus"


    /* Diary Table */
    private val DiaryTableName = "Diary"
    private val Diary_Column_ID = "DiaryID"
    private val Diary_Column_UserID = "UserID"
    private val Diary_Column_Content = "DiaryContent"
    private val Diary_Column_Title = "DiaryTitle"
    private val Diary_Column_EntryDate = "DiaryEntryDate"
    private val Diary_Column_FaceID = "DiaryFaceID"



    /* Daily Questions Table */
    private val DailyQuestionsTableName = "DailyQuestions"
    private val DailyQuestions_Column_ID = "DailyQuestionsID"
    private val DailyQuestions_Column_UserID = "UserID"
    private val DailyQuestions_Column_EntryDate = "DailyQuestionsEntryDate"
    private val DiaryQuestions_Column_Satisfaction_Progress = "DailyQuestionsSatisfactionProgress"
    private val DiaryQuestions_Column_Grateful = "DailyQuestionsGrateful"
    private val DiaryQuestions_Column_Goal = "DailyQuestionsGoal"
    private val DiaryQuestions_Column_OverallMood = "DailyQuestionsOverallMood"


    /* Images Table */
    private val ImagesTableName = "Images"
    private val Images_Column_ID = "ImagesID"
    private val Images_Column_Data = "ImagesData"


    /* Report Table */
    private val ReportTableName = "Report"
    private val Report_Column_ID = "ReportID"
    private val Report_Column_SubmitedByUserID = "UserID"
    private val Report_Column_SubmitedByAdminID = "AdminID"
    private val Report_Column_Title = "ReportTitle"
    private val Report_Column_Description = "ReportDescription"
    private val Report_Column_Date = "ReportDate"


    override fun onCreate(db: SQLiteDatabase?) {

        //create customer table
        val sqlCreateCustomerTable: String = "CREATE TABLE " + UserTableName + " (" +
                User_Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                User_Column_FullName + " TEXT NOT NULL COLLATE NOCASE, " +
                User_Column_Email + " TEXT NOT NULL UNIQUE COLLATE NOCASE, " +
                User_Column_PhoneNo + " TEXT NOT NULL, " +
                User_Column_UserName + " TEXT NOT NULL UNIQUE COLLATE NOCASE, " +
                User_Column_Password + " TEXT NOT NULL, " +
                User_Column_IsActive + " INTEGER NOT NULL )"

        db?.execSQL(sqlCreateCustomerTable)

        //Crete Admin table
        val sqlCreateAdminTable: String = "CREATE TABLE " + AdminTableName + " (" +
                Admin_Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Admin_Column_FullName + " TEXT NOT NULL COLLATE NOCASE, " +
                Admin_Column_Email + " TEXT NOT NULL UNIQUE COLLATE NOCASE, " +
                Admin_Column_PhoneNo + " TEXT NOT NULL UNIQUE, " +
                Admin_Column_UserName + " TEXT NOT NULL UNIQUE COLLATE NOCASE, " +
                Admin_Column_Password + " TEXT NOT NULL )"

        db?.execSQL(sqlCreateAdminTable)

        //Create Folder table
        val sqlCreateFolderTable = "CREATE TABLE " + FolderTableName + " (" +
                Folder_Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Folder_Column_UserID + " INTEGER NOT NULL, " +
                Folder_Column_Name + " TEXT NOT NULL, " +
                Folder_Column_Type + " TEXT NOT NULL, " +
                Folder_Column_Favorites + " INTEGER NOT NULL )"

        db?.execSQL(sqlCreateFolderTable)

        // Create Tasks table
        val sqlCreateTasksTable = "CREATE TABLE " + TasksTableName + " (" +
                Tasks_Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Tasks_Column_UserID + " INTEGER NOT NULL, " +
                Tasks_Column_FolderID + " INTEGER NOT NULL, " +
                Tasks_Column_Description + " TEXT NOT NULL, " +
                Tasks_Column_DueDate + " TEXT NOT NULL, " +
                Tasks_Column_Priority + " TEXT NOT NULL, " +
                Tasks_Column_Status + " INTEGER NOT NULL )"

        db?.execSQL(sqlCreateTasksTable)

        // Create Diary table
        val sqlCreateDiaryTable = "CREATE TABLE " + DiaryTableName + " (" +
                Diary_Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Diary_Column_UserID + " INTEGER NOT NULL, " +
                Diary_Column_Content + " TEXT NOT NULL, " +
                Diary_Column_Title + " TEXT NOT NULL, " +
                Diary_Column_EntryDate + " INTEGER NOT NULL, " +
                Diary_Column_FaceID + " INTEGER NOT NULL )"

        db?.execSQL(sqlCreateDiaryTable)

        // Create Daily Questions table
        val sqlCreateDiaryQuestionsTable = "CREATE TABLE " + DailyQuestionsTableName + " (" +
                DailyQuestions_Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DailyQuestions_Column_UserID + " INTEGER NOT NULL, " +
                DailyQuestions_Column_EntryDate + " INTEGER NOT NULL UNIQUE, " +
                DiaryQuestions_Column_Satisfaction_Progress + " TEXT NOT NULL, " +
                DiaryQuestions_Column_Grateful + " TEXT NOT NULL, " +
                DiaryQuestions_Column_Goal + " TEXT NOT NULL, " +
                DiaryQuestions_Column_OverallMood + " INTEGER NOT NULL )"

        db?.execSQL(sqlCreateDiaryQuestionsTable)

        // Create Images table
        val sqlCreateImagesTable = "CREATE TABLE " + ImagesTableName + " (" +
                Images_Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Images_Column_Data + " BLOB NOT NULL )"

        db?.execSQL(sqlCreateImagesTable)

        // Create Report table
        val sqlCreateReportTable = "CREATE TABLE " + ReportTableName + " (" +
                Report_Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Report_Column_SubmitedByUserID + " INTEGER NOT NULL, " +
                Report_Column_SubmitedByAdminID + " INTEGER NOT NULL, " +
                Report_Column_Title + " TEXT NOT NULL, " +
                Report_Column_Description + " TEXT NOT NULL, " +
                Report_Column_Date + " TEXT NOT NULL ) "

        db?.execSQL(sqlCreateReportTable)


    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    //Customer functions

    fun addUser(user: User): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(User_Column_FullName, user.UserFullName)
        cv.put(User_Column_Email, user.UserEmail)
        cv.put(User_Column_PhoneNo, user.UserPhoneNo)
        cv.put(User_Column_Password, user.UserPassword)
        cv.put(User_Column_UserName, user.UserUserName)
        cv.put(User_Column_IsActive, user.UserIsActive)

        val success = db.insert(UserTableName, null, cv)
        db.close()
        return success != -1L
    }

    fun deleteUser(user: User): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val result =
            db.delete(UserTableName, "$User_Column_ID = ${user.UserId}", null) == 1
        db.close()
        return result
    }

    fun updateUser(user: User): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(User_Column_FullName, user.UserFullName)
        cv.put(User_Column_Email, user.UserEmail)
        cv.put(User_Column_PhoneNo, user.UserPhoneNo)
        cv.put(User_Column_Password, user.UserPassword)
        cv.put(User_Column_UserName, user.UserUserName)

        val result =
            db.update(UserTableName, cv, "$User_Column_ID = ${user.UserId}", null) == 1
        db.close()
        return result
    }

    fun getUserId(username: String): Int {
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $UserTableName WHERE $User_Column_UserName = ?"
        val param = arrayOf(username)
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        return cursor.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(User_Column_ID)
                it.getInt(columnIndex)
            } else {
                -1
            }
        }
    }

    fun getUser(eID: Int): User {
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $UserTableName WHERE $User_Column_ID = ?"
        val param = arrayOf(eID.toString())
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        if (cursor.moveToFirst()) {
            db.close()
            return User(
                cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getInt(6) == 1
            )
        } else {
            db.close()
            return User(0, "Customer not exist", "", "", "", "", false)
        }
    }

    fun getUserByUsername(username: String): User? {
        val db: SQLiteDatabase = this.readableDatabase
        var user: User? = null
        val selectionArgs = arrayOf(username)
        val cursor = db.query(UserTableName, null, "$User_Column_UserName = ?", selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            user = User(
                cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getInt(6) == 1
            )
        }

        cursor.close()
        db.close()
        return user
    }


    fun getUserByEmail(userEmail: String): User {
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $UserTableName WHERE $User_Column_Email = ?"

        val param = arrayOf(userEmail)
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        if (cursor.moveToFirst()) {
            db.close()
            return User(
                cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getInt(6) == 1
            )
        } else {
            db.close()
            return User(0, "Customer not exist", "", "", "", "", false)
        }
    }

    fun checkUsernameExists(username: String): Boolean{
        val db = this.readableDatabase
        val cursor = db.query("User", arrayOf("UserUserName"),
            "UserUserName = ?", arrayOf(username), null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun checkEmailExists(email: String): Boolean{
        val db = this.readableDatabase
        val cursor = db.query("User", arrayOf("UserEmail"),
            "UserEmail = ?", arrayOf(email), null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    //admin functions
    fun deleteAdmin(admin: Admin): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val result = db.delete(AdminTableName, "$Admin_Column_ID = ${admin.AdminId}", null) == 1

        db.close()
        return result
    }

    fun updateAdmin(admin: Admin): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(Admin_Column_FullName, admin.AdminFullName)
        cv.put(Admin_Column_Email, admin.AdminEmail)
        cv.put(Admin_Column_PhoneNo, admin.AdminPhoneNo)
        cv.put(Admin_Column_Password, admin.AdminPassword)
        cv.put(Admin_Column_UserName, admin.AdminUserName)

        val result = db.update(AdminTableName, cv, "$Admin_Column_ID = ${admin.AdminId}", null) == 1

        db.close()
        return result
    }

    fun getAdminId(username: String): Int {
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $AdminTableName WHERE $Admin_Column_UserName = ?"
        val param = arrayOf(username)
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        return cursor.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(Admin_Column_ID)
                it.getInt(columnIndex)
            } else {
                -1
            }
        }
    }

    fun getAdminByUsername(username: String): Admin? {
        val db: SQLiteDatabase = this.readableDatabase
        var admin: Admin? = null
        val selectionArgs = arrayOf(username)
        val cursor = db.query(AdminTableName, null, "$Admin_Column_UserName = ?", selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            admin = Admin(
                cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5)
            )
        }

        cursor.close()
        db.close()
        return admin
    }

    fun getAdmin(eID: Int): Admin {
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $AdminTableName WHERE $Admin_Column_ID = ?"

        val param = arrayOf(eID.toString())
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        if (cursor.moveToFirst()) {
            db.close()
            return Admin(
                cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5)
            )
        } else {
            db.close()
            return Admin(0, "Admin not exist", "", "", "", "")
        }
    }



    fun addFolder(folder: Folder): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(Folder_Column_UserID, folder.UserID)
        cv.put(Folder_Column_Name, folder.FolderName)
        cv.put(Folder_Column_Type, folder.FolderType)
        cv.put(Folder_Column_Favorites, folder.isFavorite)

        val success = db.insert(FolderTableName, null, cv)
        db.close()
        return success != -1L
    }

    fun getfolders():Cursor? {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $FolderTableName", null)
        return cursor
    }

    fun getTaskCountForFolder(folderId: Int): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM Tasks WHERE FolderID = ?", arrayOf(folderId.toString()))
        var taskCount = 0
        if (cursor.moveToFirst()) {
            taskCount = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return taskCount
    }

    fun updateFolderFavoriteStatus(folderId: Int, isFavorite: Boolean) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(Folder_Column_Favorites, if (isFavorite) 1 else 0)
        }
        db.update(FolderTableName, values, "$Folder_Column_ID = ?", arrayOf(folderId.toString()))
        db.close()
    }

    fun addTasks(tasks: Tasks): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(Tasks_Column_UserID, tasks.UserID)
        cv.put(Tasks_Column_FolderID, tasks.FolderID)
        cv.put(Tasks_Column_Description, tasks.TasksDescription)
        cv.put(Tasks_Column_DueDate, tasks.TasksDueDate)
        cv.put(Tasks_Column_Priority, tasks.TasksPriority)
        cv.put(Tasks_Column_Status, tasks.TasksStatus)

        val success = db.insert(TasksTableName, null, cv)
        db.close()
        return success != -1L
    }

    fun deleteTask(taskId: Int): Boolean {
        val db = this.writableDatabase
        val affectedRows = db.delete(TasksTableName, "TasksId = ?", arrayOf(taskId.toString()))
        db.close()
        return affectedRows > 0
    }

    fun deleteFolder(folderId: Int): Boolean {
        val db = this.writableDatabase
        val affectedRows = db.delete(FolderTableName, "FolderId = ?", arrayOf(folderId.toString()))
        db.close()
        return affectedRows > 0
    }

    fun deleteAllTasksInFolder(folderId: Int): Boolean {
        val db = this.writableDatabase
        return db.delete(TasksTableName, "FolderId = ?", arrayOf(folderId.toString())) > 0
    }


    fun getTasks():Cursor? {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TasksTableName", null)
        return cursor
    }


    fun updateTaskStatus(task: Tasks) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(Tasks_Column_Status, if (task.TasksStatus) 1 else 0)
        }
        db.update(TasksTableName, contentValues, "$Tasks_Column_ID = ?", arrayOf(task.TasksId.toString()))
        db.close()
    }

    fun insertDiaryEntry(diaryEntry: Diary): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Diary_Column_UserID, diaryEntry.UserID)
            put(Diary_Column_Content, diaryEntry.DiaryContent)
            put(Diary_Column_Title, diaryEntry.DiaryTitle)
            put(Diary_Column_EntryDate, diaryEntry.DiaryEntryDate)
            put(Diary_Column_FaceID, diaryEntry.DiaryFaceID)
        }
        val result = db.insert(DiaryTableName, null, values)
        db.close()
        return result != -1L
    }


    fun getDiaryEntries():Cursor? {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $DiaryTableName", null)
        return cursor
    }

    fun updateDiaryEntry(diaryEntry: Diary): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("DiaryTitle", diaryEntry.DiaryTitle)
        contentValues.put("DiaryContent", diaryEntry.DiaryContent)
        contentValues.put("DiaryFaceID", diaryEntry.DiaryFaceID)

        val success = db.update("Diary", contentValues, "DiaryID = ?", arrayOf(diaryEntry.DiaryID.toString()))
        db.close()
        return success > 0
    }

    fun deleteDiaryEntry(diaryId: Int): Boolean {
        val db = this.writableDatabase
        val selection = "$Diary_Column_ID = ?"
        val selectionArgs = arrayOf(diaryId.toString())
        return db.delete(DiaryTableName, selection, selectionArgs) > 0
    }

    fun insertDailyQEntry(dailyQEntry: DailyQ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DailyQuestions_Column_UserID, dailyQEntry.UserID)
            put(DailyQuestions_Column_EntryDate, dailyQEntry.DailyQuestionsEntryDate)
            put(DiaryQuestions_Column_Satisfaction_Progress, dailyQEntry.DailyQuestionsSatisfactionProgress)
            put(DiaryQuestions_Column_Grateful, dailyQEntry.DailyQuestionsGrateful)
            put(DiaryQuestions_Column_Goal, dailyQEntry.DailyQuestionsGoal)
            put(DiaryQuestions_Column_OverallMood, dailyQEntry.DailyQuestionsOverallMood)
        }
        val result = db.insert(DailyQuestionsTableName, null, values)
        db.close()
        return result != -1L
    }

    fun getDailyQEntries():Cursor? {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $DailyQuestionsTableName", null)
        return cursor
    }

    fun deleteDailyQEntry(dailyQID: Int): Boolean {
        val db = this.writableDatabase
        val selection = "$DailyQuestions_Column_ID = ?"
        val selectionArgs = arrayOf(dailyQID.toString())
        return db.delete(DailyQuestionsTableName, selection, selectionArgs) > 0
    }


    fun getAllImages(): List<Images> {
        val imageList = mutableListOf<Images>()
        val db = this.readableDatabase
        val cursor = db.query(ImagesTableName, arrayOf(Images_Column_ID, Images_Column_Data), null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(Images_Column_ID))
                val imageData = cursor.getBlob(cursor.getColumnIndexOrThrow(Images_Column_Data))
                imageList.add(Images(id, imageData))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return imageList
    }
    fun addImage(imageData: ByteArray): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(Images_Column_Data, imageData)

        val success = db.insert(ImagesTableName, null, contentValues)
        db.close()
        return success != -1L
    }

    fun getLastInsertedImageId(): Int {
        val db = this.readableDatabase
        val cursor = db.query("Images", arrayOf("ImagesID"), null, null, null, null, "ImagesID DESC", "1")
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
            cursor.close()
            return id
        }
        cursor.close()
        return -1
    }

    fun deleteImage(imageId: Int): Boolean {
        val db = this.writableDatabase
        val success = db.delete(ImagesTableName, "$Images_Column_ID=?", arrayOf(imageId.toString()))
        db.close()
        return success > 0
    }

    fun addReport(report: Report): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
        put(Report_Column_SubmitedByUserID, report.UserID)
        put(Report_Column_SubmitedByAdminID, report.AdminID)
        put(Report_Column_Title, report.ReportTitle)
        put(Report_Column_Description, report.ReportDescription)
        put(Report_Column_Date, report.ReportDate)
        }

        val success = db.insert(ReportTableName, null, values)
        db.close()
        return success
    }

    fun getAllReports():Cursor? {
        val db: SQLiteDatabase = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $ReportTableName", null)
        return cursor
    }


    fun getReport(reportID: Int): Report {
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $ReportTableName WHERE $Report_Column_ID = ?"

        val param = arrayOf(reportID.toString())
        val cursor: Cursor = db.rawQuery(sqlStatement, param)

        if (cursor.moveToFirst()) {
            db.close()
            return Report(
                cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5)
            )
        } else {
            db.close()
            return Report(0, 0, 0, "Report doesn't exist", "", "")
        }
    }

    fun updateReport(reportId: Int, description: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Report_Column_Description, description)

        val success = db.update(ReportTableName, contentValues, "$Report_Column_ID = ?", arrayOf(reportId.toString()))
        db.close()
        return success > 0
    }

}