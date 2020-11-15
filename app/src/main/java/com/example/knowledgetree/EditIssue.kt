package com.example.knowledgetree

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class EditIssue : AppCompatActivity() {
    private var issueId: String? = null
    private var issueUpdatedMsg = "Issue Successfully Updated!"
    private var issueUpdateFailureMsg = "Database Error! Issue Updated Failed!"
    private var issueRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("issues")
    private var issueUpdateListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_issue)

        var addNewTaskButton = findViewById<Button>(R.id.issue_edit_add_new_task_button)
        val issue = intent.getParcelableExtra<Issue>("issue")
        issueId = intent.getStringExtra("issueId")
        issueUpdateListener = getIssueUpdateListener()
        val progressBar = findViewById<ProgressBar>(R.id.issue_edit_progress_bar)
        val title = findViewById<EditText>(R.id.issue_create_title_input_text)
        val type = findViewById<EditText>(R.id.issue_create_type_input_text)
        val detail = findViewById<EditText>(R.id.issue_create_detail_input_text)
        val complete = findViewById<CheckBox>(R.id.issue_create_complete_checkbox)

        if(issueId.isNullOrEmpty()){
            issue?.let{
                issueId = it.issueId
                title.setText(it.title)
                type.setText(it.type)
                detail.setText(it.detail)
                complete.isChecked = it.completed
                progressBar.setProgress(it.progress, true)
            }
        }else{
            issueRef.orderByChild("issueId").equalTo(issueId)
            issueRef.addValueEventListener(issueUpdateListener as ValueEventListener)
        }

        progressBar.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            intent.putExtra("issueId", issueId)
            startActivityForResult(intent, 1)
        }

        addNewTaskButton.setOnClickListener{
            val intent = Intent(this, EditTask::class.java)
            intent.putExtra("issueId", issueId)
            startActivityForResult(intent, 1)
        }
    }

    private fun getIssueUpdateListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val issue: Issue? =
                        postSnapshot.getValue(Issue::class.java)
                    if (issue != null && issue.issueId == issueId) {
                        val progressBar = findViewById<ProgressBar>(R.id.issue_edit_progress_bar)
                        val title = findViewById<EditText>(R.id.issue_create_title_input_text)
                        val type = findViewById<EditText>(R.id.issue_create_type_input_text)
                        val detail = findViewById<EditText>(R.id.issue_create_detail_input_text)
                        val complete = findViewById<CheckBox>(R.id.issue_create_complete_checkbox)
                        issueId = issue.issueId
                        title.setText(issue.title)
                        type.setText(issue.type)
                        detail.setText(issue.detail)
                        complete.isChecked = issue.completed
                        progressBar.setProgress(issue.progress, true)

                        Toast.makeText(
                            applicationContext,
                            issueUpdatedMsg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    issueUpdateFailureMsg,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val checkBox = findViewById<CheckBox>(R.id.issue_create_complete_checkbox)
        val progressBar = findViewById<ProgressBar>(R.id.issue_edit_progress_bar)
        //checkBox.isChecked = true
        //progressBar.progress = 100
        if (resultCode != Activity.RESULT_OK) return
    }
}