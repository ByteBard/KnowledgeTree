package com.example.knowledgetree

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.advanced_filtering)

        val addNewIssueBtn = findViewById<FloatingActionButton>(R.id.add_new_issue_fab)
        val applyAndFindBtn = findViewById<Button>(R.id.apply_find_btn)

        addNewIssueBtn.setOnClickListener {
            val intent = Intent(this, AddNewIssue::class.java)
            intent.putExtra("Add", "Add")
            startActivityForResult(intent, 1)
        }

        applyAndFindBtn.setOnClickListener {
            val intent = Intent(this, IssueListActivity::class.java)
            intent.putExtra("filter", "filter")
            startActivityForResult(intent, 1)
        }

    }
}