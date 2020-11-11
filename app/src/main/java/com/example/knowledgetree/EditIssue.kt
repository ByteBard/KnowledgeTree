package com.example.knowledgetree

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class EditIssue : AppCompatActivity() {
    private var issueId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_issue)

        val issue = intent.getParcelableExtra<Issue>("issue")
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val title = findViewById<EditText>(R.id.issueTitleEditIuput)
        val type = findViewById<EditText>(R.id.issueTypeEditInput)
        val detail = findViewById<EditText>(R.id.issueDetailEditInput)
        val complete = findViewById<CheckBox>(R.id.completeCheck)

        issue?.let{
            issueId = it.issueId
            title.setText(it.title)
            type.setText(it.type)
            detail.setText(it.detail)
            complete.isChecked = it.completed
            progressBar.setProgress(it.progress, true)
        }

        progressBar.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            intent.putExtra("issueId", issueId)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val checkBox = findViewById<CheckBox>(R.id.completeCheck)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        checkBox.isChecked = true
        progressBar.progress = 100
        if (resultCode != Activity.RESULT_OK) return
    }
}