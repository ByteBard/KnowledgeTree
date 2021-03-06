package com.example.knowledgetree

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.database.*
import java.util.*

class EditIssue : AppCompatActivity() {
    private var issueId: String? = null
    private var issue: Issue? = null
    private var issueUpdatedMsg = "Issue Successfully Updated!"
    private var issueUpdateFailureMsg = "Database Error! Issue Updated Failed!"
    private var issueRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("issues")
    private var issueUpdateListener: ValueEventListener? = null
    private var removedMsg = "Issue Successfully Removed!"
    private var isValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_issue)

        var addNewTaskButton = findViewById<Button>(R.id.issue_edit_add_new_task_button)
        var updateIssueButton = findViewById<Button>(R.id.issue_edit_update_button)
        var removeIssueImageViewButton = findViewById<ImageView>(R.id.issue_edit_remove_button)
        issue = intent.getParcelableExtra<Issue>("issue")
        issueId = intent.getStringExtra("issueId")
        issueUpdateListener = getIssueUpdateListener()
        val progressBar = findViewById<ProgressBar>(R.id.issue_edit_progress_bar)
        val title = findViewById<EditText>(R.id.issue_edit_title_input_text)
        val type = findViewById<EditText>(R.id.issue_edit_type_input_text)
        val detail = findViewById<EditText>(R.id.issue_edit_detail_input_text)
        val complete = findViewById<CheckBox>(R.id.issue_create_complete_checkbox)

        if (issueId.isNullOrEmpty()) {
            issue?.let {
                issueId = it.issueId
                title.setText(it.title)
                type.setText(it.type)
                detail.setText(it.detail)
                complete.isChecked = it.completed
                progressBar.setProgress(it.progress, true)
            }
        } else {
            issueRef.orderByChild("issueId").equalTo(issueId)
            issueRef.addValueEventListener(issueUpdateListener as ValueEventListener)
        }

        progressBar.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            intent.putExtra("issueId", issueId)
            startActivityForResult(intent, 1)
        }

        addNewTaskButton.setOnClickListener {
            val intent = Intent(this, EditTask::class.java)
            intent.putExtra("issueId", issueId)
            startActivityForResult(intent, 1)
        }

        removeIssueImageViewButton.setOnClickListener{
            issue?.let{
                val dr = issueRef.child(it.issueId)
                dr.removeValue();
            }
            finish()
        }

        updateIssueButton.setOnClickListener {
            validateInput()
            if(isValid){
                val title = findViewById<EditText>(R.id.issue_edit_title_input_text).text.toString()
                val type = findViewById<EditText>(R.id.issue_edit_type_input_text).text.toString()
                val detail = findViewById<EditText>(R.id.issue_edit_detail_input_text).text.toString()
                val complete = findViewById<CheckBox>(R.id.issue_create_complete_checkbox).isChecked
                val currentTime: Date = Calendar.getInstance().time
                issue?.let {
                    val updatedIssue = Issue(
                        it.issueId,
                        title,
                        detail,
                        type,
                        it.progress,
                        complete,
                        it.createdTimestamp,
                        currentTime
                    )

                    issueRef.child(it.issueId).setValue(updatedIssue)
                        .addOnSuccessListener {
                            Toast.makeText(
                                applicationContext,
                                issueUpdatedMsg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                applicationContext,
                                issueUpdateFailureMsg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                Toast.makeText(
                    applicationContext,
                    removedMsg,
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun validateInput(){
        val titleEditView = findViewById<EditText>(R.id.issue_edit_title_input_text)
        val typeEditView = findViewById<EditText>(R.id.issue_edit_type_input_text)
        val detailView = findViewById<EditText>(R.id.issue_edit_detail_input_text)
        when {
            titleEditView.text.isNullOrEmpty() -> {
                titleEditView.error = "Title can not be empty!"
                titleEditView.requestFocus()
                isValid = false
            }
            typeEditView.text.isNullOrEmpty() -> {
                typeEditView.error = "Type can not be empty!"
                typeEditView.requestFocus()
                isValid = false
            }
            detailView.text.isNullOrEmpty() -> {
                detailView.error = "Detail can not be empty!"
                detailView.requestFocus()
                isValid = false
            }
            else -> {
                isValid = true
            }
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
                        val title = findViewById<EditText>(R.id.issue_edit_title_input_text)
                        val type = findViewById<EditText>(R.id.issue_edit_type_input_text)
                        val detail = findViewById<EditText>(R.id.issue_edit_detail_input_text)
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
        if (resultCode != Activity.RESULT_OK) return
    }
}