package com.example.knowledgetree

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskListAdaptor(
    private val taskList: List<Task>,
    private val listener: (Task) -> Unit

) : RecyclerView.Adapter<TaskListAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.task_item, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = taskList.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = taskList[position]
        holder.bind(item)
    }

    inner class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val taskName = v.findViewById<TextView>(R.id.task_name)
        val taskComplete = v.findViewById<ImageView>(R.id.task_item_complete_checkbox)

        fun bind(task: Task) {
            taskName.text = task.name
            if(task.completed){
                taskComplete.setImageResource(R.drawable.task_checked)
            }else{
                taskComplete.setImageResource(R.drawable.task_unchecked)
            }
            v.setOnClickListener { listener(task) }
        }
    }
}