package com.example.EmpowerTasksAndMood.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Model.Diary
import com.example.EmpowerTasksAndMood.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiaryFolderAdapter(private val diaryEntries: List<Diary>) :
    RecyclerView.Adapter<DiaryFolderAdapter.DiaryViewHolder>()  {

    class DiaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitleFolderDiary)
        val entryDateTextView: TextView = itemView.findViewById(R.id.textViewFolderDateDiaryCreated)
        val face: ImageView = itemView.findViewById(R.id.faceImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_diary_folders, parent, false)
        return DiaryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return diaryEntries.size
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val currentEntry = diaryEntries[position]
        holder.titleTextView.text = currentEntry.DiaryTitle
        holder.entryDateTextView.text = formatDiaryEntryDate(currentEntry.DiaryEntryDate)

        val faceDrawableId = getFaceDrawableId(currentEntry.DiaryFaceID)
        holder.face.setImageResource(faceDrawableId)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, com.example.EmpowerTasksAndMood.Diary::class.java).apply {
                putExtra("DiaryID", currentEntry.DiaryID)
                putExtra("UserID", currentEntry.UserID)
                putExtra("DiaryContent", currentEntry.DiaryContent)
                putExtra("DiaryTitle", currentEntry.DiaryTitle)
                putExtra("DiaryEntryDate", currentEntry.DiaryEntryDate)
                putExtra("DiaryFaceID", currentEntry.DiaryFaceID)
            }
            context.startActivity(intent)
        }
    }
    private fun getFaceDrawableId(selectedFaceId: Int): Int {
        return when (selectedFaceId) {
            1 -> R.drawable.baseline_sentiment_sad_24
            2 -> R.drawable.baseline_sentiment_neutral_24
            3 -> R.drawable.baseline_sentiment_happy_24
            4 -> R.drawable.baseline_sentiment_very_happy_24

            else -> R.drawable.baseline_sentiment_very_happy_24
        }
    }

    private fun formatDiaryEntryDate(timestamp: Int): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp * 1000L))
    }
}
