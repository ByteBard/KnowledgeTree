package com.example.knowledgetree

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class AddNewIssue : AppCompatActivity() {
    private var issueRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("issues")
    private var issueSuccessfulCreateToastMsg = "Issue Successfully Created!"
    private var issueCreateFailToastMsg = "Issue Create Fail!"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_issue)

        val createNewIssueButton = findViewById<Button>(R.id.issue_create_create_button)
        createNewIssueButton.setOnClickListener{
            val issueTitle = findViewById<EditText>(R.id.issue_create_title_input_text).text.toString()
            val issueType = findViewById<EditText>(R.id.issue_create_type_input_text).text.toString()
            val issueDetail = findViewById<EditText>(R.id.issue_create_detail_input_text).text.toString()
            val issueCompleted = findViewById<CheckBox>(R.id.issue_create_complete_checkbox).isChecked
            val newIssueId = issueRef.push().key.toString()
            val newIssue = Issue(newIssueId, issueTitle, issueDetail, issueType, getProgressByCheckBoxStatus(issueCompleted), issueCompleted, Calendar.getInstance().time, Calendar.getInstance().time)
            issueRef.child(newIssueId).setValue(newIssue).addOnSuccessListener {
                Toast.makeText(applicationContext, issueSuccessfulCreateToastMsg, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, IssueListActivity::class.java)
                startActivityForResult(intent, 1)
            }.addOnFailureListener{
                Toast.makeText(applicationContext, issueCreateFailToastMsg, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, IssueListActivity::class.java)
                startActivityForResult(intent, 1)
            }
        }
    }

    private fun getProgressByCheckBoxStatus(issueCompleted: Boolean): Int {
        return if (issueCompleted) 100 else 0
    }
}