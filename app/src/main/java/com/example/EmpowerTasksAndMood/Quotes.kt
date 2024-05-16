package com.example.EmpowerTasksAndMood

import ImageAdapter
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Images
import java.io.File
import java.io.IOException
import android.Manifest
import androidx.appcompat.app.AlertDialog

class Quotes : AppCompatActivity() {
    private lateinit var imageRecyclerView: RecyclerView
    private lateinit var saveQuoteButton: Button
    private lateinit var deleteQuoteButton: Button

    private val dbHelper: DBHelper = DBHelper(this)
    private var userId: Int = 0
    private var adminId: Int = 0
    private val images = mutableListOf<Images>()

    companion object {
        private const val IMAGE_PICK_CODE = 999
        private const val PERMISSION_CODE = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userId = intent.getIntExtra("user_ID", 0)
        adminId = intent.getIntExtra("admin_ID", 0)

        if(adminId != 0) {
            setContentView(R.layout.activity_admin_quotes)
            deleteQuoteButton = findViewById(R.id.deleteQuote)
            deleteListener()
        } else {
            setContentView(R.layout.activity_quotes)
        }

        imageRecyclerView = findViewById(R.id.recycleViewQuotes)
        imageRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(imageRecyclerView)

        images.addAll(dbHelper.getAllImages())
        imageRecyclerView.adapter = ImageAdapter(this, images, this::onImageSave)

        saveQuoteButton = findViewById(R.id.saveQuote)

        saveListener()
    }

    private fun saveListener() {
        saveQuoteButton.setOnClickListener {
            val currentPosition = (imageRecyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (currentPosition != RecyclerView.NO_POSITION) {
                onImageSave(images[currentPosition])
            }
        }
    }

    private fun deleteListener() {
        deleteQuoteButton.setOnClickListener {
            val currentPosition = (imageRecyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (currentPosition != RecyclerView.NO_POSITION) {
                onImageDelete(images[currentPosition])
            }
        }
    }

    fun displayImages() {
        val imagesFromDB = dbHelper.getAllImages()
        imageRecyclerView.adapter = ImageAdapter(this, imagesFromDB) { image ->
            onImageSave(image)
        }

    }
    private fun onImageSave(image: Images) {
        val filename = "Quote_${image.ImagesID}.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "EmpowerMoodQuotes")
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        try {
            contentResolver.openOutputStream(uri ?: return).use { outputStream ->
                outputStream ?: return
                outputStream.write(image.ImagesData)
            }
            uri?.let { MediaScannerConnection.scanFile(this, arrayOf(it.toString()), null, null) }
            Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onImageDelete(image: Images) {
        AlertDialog.Builder(this)
            .setTitle("Delete Image")
            .setMessage("Are you sure you want to delete this image?")
            .setPositiveButton("Yes") { dialog, which ->
                val success = dbHelper.deleteImage(image.ImagesID)
                if (success) {
                    val position = images.indexOf(image)
                    if (position != -1) {
                        images.removeAt(position)
                    }
                    Toast.makeText(this, "Images deleted successfully", Toast.LENGTH_SHORT).show()
                    displayImages()
                } else {
                    Toast.makeText(this, "Error deleting image", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    fun addImage(view: View) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions, PERMISSION_CODE)
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val imageUri = data?.data ?: return
            contentResolver.openInputStream(imageUri)?.use { inputStream ->
                val imageData = inputStream.readBytes()

                if (dbHelper.addImage(imageData)) {
                    val newImageId = dbHelper.getLastInsertedImageId()
                    val newImage = Images(newImageId, imageData)
                    images.add(newImage)
                    imageRecyclerView.adapter?.notifyItemInserted(images.size - 1)
                    Toast.makeText(this, "Image added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to add image to database", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun menuPage(view: View){
        val intent = Intent(this, Menu::class.java)
        intent.putExtra("user_ID", userId)
        startActivity(intent)
    }

    fun mainPageAdmin(view: View){
        val intent = Intent(this, Admin::class.java)
        intent.putExtra("admin_ID", adminId)
        startActivity(intent)
    }
}

