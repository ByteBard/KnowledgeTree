package com.example.knowledgetree

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.util.*

class EditTask : AppCompatActivity() {
    private var successfulUpdateToastMsg = "Task Successfully Updated!"
    private var successfulCreatedToastMsg = "Task Successfully Created!"
    private var failUpdateToastMsg = "Task Updated Failure!"
    private var failCreateToastMsg = "Task Create Failure!"
    private var errorMsg = "Something is Wrong!"
    private var removedMsg = "Task Successfully Removed!"
    private var issueProgressUpdateSuccessMsg = "Issue Progress Updated Successfully!"
    private var issueResolvedMsg = "Issue Resolved"
    private var dataChangeCancelMsg = "DatabaseError! Operation Cancelled"
    private lateinit var mode: Mode
    private var task: Task? = null
    private var issueId: String? = null
    private var taskRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("tasks")
    private var issueRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("issues")
    private var taskUpdateListener: ValueEventListener? = null
    private var updatedProgress: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_task)
        issueId = intent.getStringExtra("issueId")
        val passedTask = intent.getParcelableExtra<Task>("task")
        setModeByPassIntent(passedTask)
        val taskEditSaveBtn = findViewById<Button>(R.id.task_edit_update_button)
        val taskEditCancelBtn = findViewById<Button>(R.id.task_edit_cancel_button)
        val taskRemoveImageViewButton = findViewById<ImageView>(R.id.task_edit_remove_button)
        taskEditCancelBtn.setOnClickListener {
            finish()
        }
        setButtonTextByMode(taskEditSaveBtn)
        val taskDetailView = findViewById<EditText>(R.id.task_edit_detail)
        val taskCompleteCheckBox = findViewById<CheckBox>(R.id.taskCompleteCheckBox)
        taskUpdateListener = getTaskUpdateListener()
        passedTask?.let { it ->
            task = it
            taskDetailView.setText(it.name)
            taskCompleteCheckBox.isChecked = it.completed
        }

        taskRemoveImageViewButton.setOnClickListener{
            task?.let{
                val dr = taskRef.child(it.taskId)
                dr.removeValue()
                refreshIssueStatus(it.issueId)
            }
            finish()
            Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
        }

        taskEditSaveBtn.setOnClickListener {
            val taskDetailView = findViewById<EditText>(R.id.task_edit_detail)
            val taskCompleteCheckBox = findViewById<CheckBox>(R.id.taskCompleteCheckBox)
            val taskDetail = taskDetailView.text.toString()
            val taskComplete = taskCompleteCheckBox.isChecked
            val currentTime: Date = Calendar.getInstance().time

            if (!issueId.isNullOrEmpty() && task == null && mode == Mode.Create) {
                val newTaskId = taskRef.push().key.toString()
                val newTask = Task(
                    newTaskId,
                    issueId.toString(),
                    taskDetail,
                    taskComplete,
                    currentTime,
                    currentTime
                )

                taskRef.child(newTaskId).setValue(newTask)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, successfulCreatedToastMsg, Toast.LENGTH_SHORT).show()
                        refreshIssueStatus(issueId.toString())
                        val intent = Intent(this, EditIssue::class.java)
                        intent.putExtra("issueId", issueId.toString())
                        startActivityForResult(intent, 1)
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, failCreateToastMsg, Toast.LENGTH_SHORT).show()
                    }
            } else if (task != null && mode == Mode.Edit) {
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
                            refreshIssueStatus(issueId.toString())
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                applicationContext,
                                failUpdateToastMsg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            } else {
                Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }

    private fun refreshIssueStatus(passedIssueId: String) {
        if(task?.issueId.isNullOrEmpty()){
            val queryRef: Query =
                taskRef.orderByChild("issueId").equalTo(passedIssueId)
            queryRef.addValueEventListener(taskUpdateListener as ValueEventListener)
        }else{
            val queryRef: Query =
                taskRef.orderByChild("issueId").equalTo(task!!.issueId)
            queryRef.addValueEventListener(taskUpdateListener as ValueEventListener)
        }

        Toast.makeText(
            applicationContext,
            successfulUpdateToastMsg,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun getTaskUpdateListener(): ValueEventListener {
        return object : ValueEventListener {
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
                                Toast.makeText(
                                    applicationContext,
                                    issueProgressUpdateSuccessMsg,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        if (updatedProgress == 100) {
                            issueRef.child(it.issueId).child("complete").setValue(true)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        applicationContext,
                                        issueResolvedMsg,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, dataChangeCancelMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setModeByPassIntent(task: Task?) {
        mode = if (task == null) Mode.Create else Mode.Edit
    }

    private fun setButtonTextByMode(button: Button) {
        when (mode) {
            Mode.Create -> button.text = "Create"
            Mode.Edit -> button.text = "Edit"
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