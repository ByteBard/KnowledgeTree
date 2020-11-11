package com.example.knowledgetree

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import java.util.*

class EditTask : AppCompatActivity() {
    private var task: Task? = null
    private var taskRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("tasks")
    private var issueRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("issues")
    private var taskUpdateListener: ValueEventListener? = null
    private var updatedProgress: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_task)

        val passedTask = intent.getParcelableExtra<Task>("task")
        val taskDetailView = findViewById<EditText>(R.id.task_edit_detail)
        val taskCompleteCheckBox = findViewById<CheckBox>(R.id.taskCompleteCheckBox)

        taskUpdateListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var complete = 0.00
                var notComplete = 0.00
                for (postSnapshot in dataSnapshot.children) {
                    val task: Task? =
                        postSnapshot.getValue(Task::class.java)
                    if (task != null) {
                        if (task.completed) {
                            complete++
                        } else {
                            notComplete++
                        }
                    }
                }

                if (complete + notComplete != 0.00) {
                    updatedProgress = ((complete / (complete + notComplete)) * 100).toInt()

                    task?.let {
                        issueRef.child(it.issueId).child("progress").setValue(updatedProgress)
                            .addOnSuccessListener {
                                val detail1 =
                                    "progress Updated Successfully!"
                                Toast.makeText(applicationContext, detail1, Toast.LENGTH_SHORT)
                                    .show()
                            }

                        if (updatedProgress == 100) {
                            issueRef.child(it.issueId).child("complete").setValue(true)
                                .addOnSuccessListener {
                                    val detail1 =
                                        "Issue Resolved"
                                    Toast.makeText(applicationContext, detail1, Toast.LENGTH_SHORT)
                                        .show()
                                }
                        }
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        passedTask?.let { it ->
            task = it
            taskDetailView.setText(it.name)
            taskCompleteCheckBox.isChecked = it.completed
        }
        val taskEditSaveBtn = findViewById<Button>(R.id.task_edit_save)

        val detail1 =
            "Task Updated Successfully!"
        taskEditSaveBtn.setOnClickListener {
            val taskDetailView = findViewById<EditText>(R.id.task_edit_detail)
            val taskCompleteCheckBox = findViewById<CheckBox>(R.id.taskCompleteCheckBox)
            val taskDetail = taskDetailView.text.toString()
            val taskComplete = taskCompleteCheckBox.isChecked
            val currentTime: Date = Calendar.getInstance().time
            task?.let { it ->
                val updatedTask = Task(
                    it.taskId,
                    it.issueId,
                    taskDetail,
                    taskComplete,
                    it.createdTimestamp,
                    currentTime
                )
                taskRef.child(it.taskId).setValue(updatedTask)
                    .addOnSuccessListener {
                        val queryRef: Query =
                            taskRef.orderByChild("issueId").equalTo(task!!.issueId)
                        queryRef.addValueEventListener(taskUpdateListener as ValueEventListener)
                        Toast.makeText(applicationContext, detail1, Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                    }
            }
        }
    }

    override fun onBackPressed() {
        task?.let {
            val intent = intent.apply {
                putExtra("issueId", it.issueId)
            }
            setResult(Activity.RESULT_OK, intent)
        }
        super.onBackPressed()
    }
}