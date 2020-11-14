package com.example.knowledgetree

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
        val issueListStatus = v.findViewById<ImageView>(R.id.completeCheck)

        fun bind(issue: Issue) {
            issueListTitle.text = issue.title
            issueListType.text = issue.type
            issueListProgress.progress = issue.progress
            if(issue.completed){
                issueListStatus.setImageResource(R.drawable.ic_baseline_check_circle_24)
            }else{
                issueListStatus.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
            }
            v.setOnClickListener { listener(issue) }
        }
    }
}