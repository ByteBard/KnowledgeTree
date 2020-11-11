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
    private var valueEventListener: ValueEventListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_list)
        val issueId = intent.extras?.getString("issueId")
        val taskListView = findViewById<RecyclerView>(R.id.task_list)
        val list = mutableListOf<Task>()
        taskListView.adapter = TaskListAdaptor(list) { showDetail(it) }
        taskListView.layoutManager = LinearLayoutManager(this)
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val task: Task? =
                        postSnapshot.getValue(Task::class.java)
                    if (task != null) {
                        list.add(task)
                        (taskListView.adapter as TaskListAdaptor).notifyDataSetChanged()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        var ref = FirebaseDatabase.getInstance().getReference("tasks")
        issueId?.let {issueId ->
            val queryRef: Query = ref.orderByChild("issueId").equalTo(issueId)
            queryRef.addValueEventListener(valueEventListener as ValueEventListener)
        }
    }

    fun showDetail(task: Task) {
        val intent = Intent(this, EditTask::class.java)
        intent.putExtra("task", task)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val taskListView = findViewById<RecyclerView>(R.id.task_list)
        val taskList1 = mutableListOf<Task>()

        taskListView.adapter = TaskListAdaptor(taskList1) { showDetail(it) }
        taskListView.layoutManager = LinearLayoutManager(this)
        if (resultCode != Activity.RESULT_OK) return
    }
}