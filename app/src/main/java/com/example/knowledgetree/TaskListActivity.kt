package com.example.knowledgetree

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class TaskListActivity : AppCompatActivity() {
    private var valueEventListenerForTaskList: ValueEventListener? = null
    private var taskList: MutableList<Task> = mutableListOf()
    private lateinit var taskListView: RecyclerView
    private var taskRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("tasks")
    private var issueId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_list)
        issueId = intent.extras?.getString("issueId")
        taskListView = findViewById(R.id.task_list)
        taskListView.adapter = TaskListAdaptor(taskList) { showDetail(it) }
        taskListView.layoutManager = LinearLayoutManager(this)
        valueEventListenerForTaskList = getValueEventListenerForTaskList()
        getTaskListView()
    }

    private fun getValueEventListenerForTaskList(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                taskList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val task: Task? =
                        postSnapshot.getValue(Task::class.java)
                    if (task != null) {
                        taskList.add(task)
                        (taskListView.adapter as TaskListAdaptor).notifyDataSetChanged()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
    }

    private fun getTaskListView(){
        issueId?.let {issueId ->
            val queryRef: Query = taskRef.orderByChild("issueId").equalTo(issueId)
            queryRef.addValueEventListener(valueEventListenerForTaskList as ValueEventListener)
        }
    }

    private fun showDetail(task: Task) {
        val intent = Intent(this, EditTask::class.java)
        intent.putExtra("task", task)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        getTaskListView()
    }
}