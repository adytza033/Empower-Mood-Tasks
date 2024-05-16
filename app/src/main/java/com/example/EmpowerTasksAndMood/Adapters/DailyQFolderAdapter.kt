package com.example.EmpowerTasksAndMood.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Model.DailyQ
import com.example.EmpowerTasksAndMood.R
import com.example.EmpowerTasksAndMood.DailyQuestions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyQFolderAdapter(private val dailyQEntries: List<DailyQ>) :
    RecyclerView.Adapter<DailyQFolderAdapter.DailyQViewHolder>() {

    class DailyQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.textViewFolderDateDailyQCreated)
        val face: ImageView = itemView.findViewById(R.id.faceImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyQViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_daily_folders, parent, false)
        return DailyQViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dailyQEntries.size
    }

    override fun onBindViewHolder(holder: DailyQViewHolder, position: Int) {
        val currentEntry = dailyQEntries[position]
        holder.date.text = formatDiaryEntryDate(currentEntry.DailyQuestionsEntryDate)


        val faceDrawableId = getFaceDrawableId(currentEntry.DailyQuestionsOverallMood)
        holder.face.setImageResource(faceDrawableId)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DailyQuestions::class.java).apply {
                putExtra("DailyQID", currentEntry.DailyQuestionsID)
                putExtra("UserID", currentEntry.UserID)
                putExtra("EntryDate", currentEntry.DailyQuestionsEntryDate)
                putExtra("SatisfactionProgress", currentEntry.DailyQuestionsSatisfactionProgress)
                putExtra("Grateful", currentEntry.DailyQuestionsGrateful)
                putExtra("Goal", currentEntry.DailyQuestionsGoal)
                putExtra("OverallMood", currentEntry.DailyQuestionsOverallMood)
            }

            context.startActivity(intent)
        }
    }
    private fun getFaceDrawableId(selectedFaceId: Int): Int {
        return when (selectedFaceId) {
            1 -> R.drawable.baseline_sentiment_sad_24
            2 -> R.drawable.baseline_sentiment_sad2_24
            3 -> R.drawable.baseline_sentiment_neutral_24
            4 -> R.drawable.baseline_sentiment_happy_24
            5 -> R.drawable.baseline_sentiment_very_happy_24

            else -> R.drawable.baseline_sentiment_very_happy_24
        }
    }
    private fun formatDiaryEntryDate(timestamp: Int): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp * 1000L))
    }
}
