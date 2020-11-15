package com.example.knowledgetree
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class IssueListActivity : AppCompatActivity() {
    private var issue: Issue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.issue_list)

        val issueListView = findViewById<RecyclerView>(R.id.issue_list)
        var ref = FirebaseDatabase.getInstance().getReference("issues")
        val list = mutableListOf<Issue>()
        issueListView.adapter = IssueListAdaptor(list) { showDetail(it) }
        issueListView.layoutManager = LinearLayoutManager(this)
        val queryRef: Query = ref.orderByChild("title")

        queryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val issue: Issue? =
                        postSnapshot.getValue(Issue::class.java)
                    if (issue != null) {
                        list.add(issue)
                        (issueListView.adapter as IssueListAdaptor).notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_issues
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_issues -> {
                    true
                }
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("MainActivity", "MainActivity")
                    startActivityForResult(intent, 1)
                }
                R.id.navigation_task -> {
                    val intent = Intent(this, TaskListActivity::class.java)
                    intent.putExtra("TaskListActivity", "TaskListActivity")
                    startActivityForResult(intent, 1)
                }
            }
            true
        }

        val addNewIssueBtn = findViewById<FloatingActionButton>(R.id.create_new_issue_fab)
        addNewIssueBtn.setOnClickListener {
            val intent = Intent(this, AddNewIssue::class.java)
            intent.putExtra("Add", "Add")
            startActivityForResult(intent, 1)
        }

    }

    private fun showDetail(issue: Issue) {
        val intent = Intent(this, EditIssue::class.java)
        intent.putExtra("issue", issue)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val issueListView = findViewById<RecyclerView>(R.id.issue_list)
        var ref = FirebaseDatabase.getInstance().getReference("issues")
        val list = mutableListOf<Issue>()
        val queryRef: Query = ref.orderByChild("title")

        queryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val issue: Issue? =
                        postSnapshot.getValue(Issue::class.java)
                    if (issue != null) {
                        list.add(issue)
                        (issueListView.adapter as IssueListAdaptor).notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
        issueListView.adapter = IssueListAdaptor(list) { showDetail(it) }
        issueListView.layoutManager = LinearLayoutManager(this)
        if (resultCode != Activity.RESULT_OK) return
    }
}