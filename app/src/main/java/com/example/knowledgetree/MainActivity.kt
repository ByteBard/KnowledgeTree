package com.example.knowledgetree

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_home



        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_issues -> {
                    val intent = Intent(this, IssueListActivity::class.java)
                    intent.putExtra("filter", "filter")
                    startActivityForResult(intent, 1)
                }
//                R.id.action_schedules -> {
//                }
//                R.id.action_music -> {
//                }
            }
            true
        }







//        val loginBtn = findViewById<Button>(R.id.login_button)
//
//        loginBtn.setOnClickListener {
//            val intent = Intent(this, FilterActivity::class.java)
//            val currentTime = Calendar.getInstance().time
//
//
//            var ref = FirebaseDatabase.getInstance().getReference("tasks")
//            val taskList1 = mutableListOf<Task>()
//
//            val queryRef: Query = ref.orderByChild("taskId").equalTo("-MLmM8C6NPh1fK2sTkTm")
//
//
//            queryRef.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    taskList1.clear()
//                    for (postSnapshot in dataSnapshot.children) {
//                        val task: Task? =
//                            postSnapshot.getValue(Task::class.java)
//                        if (task != null) {
//                            taskList1.add(task)
//                        }
//                    }
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    println("The read failed: " + databaseError.code)
//                }
//            })
//
//
//
//
//
//
//
//            intent.putExtra("login", "login")
//            startActivityForResult(intent, 0)
//        }
    }
}