package com.example.EmpowerTasksAndMood.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.EmpowerTasksAndMood.Model.DBHelper
import com.example.EmpowerTasksAndMood.Model.Report
import com.example.EmpowerTasksAndMood.R

class ReportAdapter(var reports: List<Report>, private val dbHelper: DBHelper, private val adminId: Int) : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.reportTitleList)
        val senderUserName: TextView = view.findViewById(R.id.senderUserNameList)
        val date: TextView = view.findViewById(R.id.reportDateList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_reports, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = reports[position]
        holder.title.text = report.ReportTitle
        holder.date.text = report.ReportDate

        if(report.AdminID == 0){
            holder.senderUserName.text = dbHelper.getUser(report.UserID).UserFullName.split(" ").firstOrNull()
        } else {
            holder.senderUserName.text = dbHelper.getAdmin(report.AdminID).AdminFullName.split(" ").firstOrNull()
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, com.example.EmpowerTasksAndMood.Report::class.java).apply {
                putExtra("ReportID", report.ReportID)
                putExtra("user_ID", report.UserID)
                putExtra("admin_ID", adminId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = reports.size
}
