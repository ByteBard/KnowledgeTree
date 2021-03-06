package com.example.knowledgetree

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.util.*

class IssueListAdaptor(
    private val issueList: List<Issue>,
    private val listener: (Issue) -> Unit

) : RecyclerView.Adapter<IssueListAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.issue_item, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = issueList.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = issueList[position]
        holder.bind(item)
    }

    inner class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val issueListTitle = v.findViewById<TextView>(R.id.issueListTitle)
        val issueListType = v.findViewById<TextView>(R.id.issueListType)
        val issueListProgress = v.findViewById<ProgressBar>(R.id.issueProgress)
        val issueListProgressValue = v.findViewById<TextView>(R.id.issue_list_progress_blob)
        val issueListStatus = v.findViewById<ImageView>(R.id.issue_create_complete_checkbox)
        val issueListVisitedDate = v.findViewById<TextView>(R.id.issue_list_view_date)

        fun bind(issue: Issue) {

            val df: DateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH)
            val date = df.format(issue.lastAccessTimestamp)
            issueListTitle.text = issue.title
            issueListType.text = issue.type
            issueListProgress.progress = issue.progress
            issueListProgressValue.text = "${issue.progress}%"
            issueListVisitedDate.text = "Last Visited at: $date"
            if(issue.completed){
                issueListStatus.setImageResource(R.drawable.ic_baseline_check_circle_24)
            }else{
                issueListStatus.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
            }
            v.setOnClickListener { listener(issue) }
        }
    }
}