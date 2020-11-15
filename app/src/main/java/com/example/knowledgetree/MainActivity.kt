package com.example.knowledgetree
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private var taskRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("tasks")
    private var issueRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("issues")
    val taskList : MutableList<Task> = arrayListOf()
    val issueList : MutableList<Issue> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_home
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_issues -> {
                    val intent = Intent(this, IssueListActivity::class.java)
                    intent.putExtra("IssueListActivity", "IssueListActivity")
                    startActivityForResult(intent, 1)
                }
                R.id.navigation_home -> {
                    true
                }
                R.id.navigation_task -> {
                    val intent = Intent(this, TaskListActivity::class.java)
                    intent.putExtra("TaskListActivity", "TaskListActivity")
                    startActivityForResult(intent, 1)
                }
            }
            true
        }
        setTaskOverallView()
        setIssueOverallView()
    }

    private fun setTaskStatisticsView(){
        val taskTotalCountView = findViewById<TextView>(R.id.overall_home_task_label_total_number)
        val taskCompletedCountView = findViewById<TextView>(R.id.overall_home_task_label_complete_number)
        val taskIncompleteCountView = findViewById<TextView>(R.id.overall_home_task_label_incomplete_number)
        val taskProgressBar = findViewById<ProgressBar>(R.id.overall_home_task_progressBar)
        val taskPercentage = findViewById<TextView>(R.id.overall_home_task_label_progress)

        if(taskList.any()){
            val totalCount = taskList.count().toDouble()
            val resolvedCount = taskList.filter { x -> x.completed }.count().toDouble()
            var unresolvedCount = totalCount - resolvedCount
            val progressValue = ((resolvedCount / totalCount) * 100).toInt()

            taskTotalCountView.text = totalCount.toInt().toString()
            taskCompletedCountView.text = resolvedCount.toInt().toString()
            taskIncompleteCountView.text = unresolvedCount.toInt().toString()
            taskProgressBar.progress = progressValue
            taskPercentage.text = "$progressValue%"
        }
    }

    private fun setIssueStatisticsView(){
        val issueTotalCountView = findViewById<TextView>(R.id.overall_home_issue_total_number)
        val issueResolvedCountView = findViewById<TextView>(R.id.overall_home_issue_resolved_number)
        val issueUnresolvedCountView = findViewById<TextView>(R.id.overall_home_issue_unresolved_number)
        val issueProgressBar = findViewById<ProgressBar>(R.id.overall_home_issue_progressBar)
        val issuePercentage = findViewById<TextView>(R.id.overall_home_task_label_progress_value)

        if(issueList.any()){
            val totalCount = issueList.count().toDouble()
            val resolvedCount = issueList.filter { x -> x.completed }.count().toDouble()
            var unresolvedCount = totalCount - resolvedCount
            val progressValue = ((resolvedCount / totalCount) * 100).toInt()

            issueTotalCountView.text = totalCount.toInt().toString()
            issueResolvedCountView.text = resolvedCount.toInt().toString()
            issueUnresolvedCountView.text = unresolvedCount.toInt().toString()
            issueProgressBar.progress = progressValue
            issuePercentage.text = "$progressValue%"
        }
    }

   private fun setTaskOverallView() {
       taskRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val task: Task? =
                        postSnapshot.getValue(Task::class.java)
                    if (task != null) {
                        taskList.add(task)
                    }
                }
                setTaskStatisticsView()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun setIssueOverallView() {
        issueRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val issue: Issue? =
                        postSnapshot.getValue(Issue::class.java)
                    if (issue != null) {
                        issueList.add(issue)
                    }
                }
                setIssueStatisticsView()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        setTaskOverallView()
        setIssueOverallView()
    }
}