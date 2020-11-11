package com.example.knowledgetree

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val queryRef: Query = ref.orderByChild("userId").equalTo("-MLmFUwyHG3elhxUkXVG")

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

    }

    fun showDetail(issue: Issue) {
        val intent = Intent(this, EditIssue::class.java)
        intent.putExtra("issue", issue)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val issueListView = findViewById<RecyclerView>(R.id.issue_list)
        var ref = FirebaseDatabase.getInstance().getReference("issues")
        val list = mutableListOf<Issue>()
        val queryRef: Query = ref.orderByChild("userId").equalTo("-MLmFUwyHG3elhxUkXVG")

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