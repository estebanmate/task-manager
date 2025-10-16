package com.example.taskmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.model.Task
import com.example.taskmanager.model.TaskStatus
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val tasks = createDummyTasks()
        taskAdapter = TaskAdapter(tasks)
        recyclerView.adapter = taskAdapter

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item = menu.findItem(R.id.action_view_spinner)
        val spinner = item.actionView as Spinner

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.view_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        return true
    }

    private fun createDummyTasks(): List<Task> {
        return listOf(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description for task 1",
                startTime = Date(),
                endTime = Date(),
                status = TaskStatus.TO_DO
            ),
            Task(
                id = "2",
                title = "Task 2",
                description = "Description for task 2",
                startTime = Date(),
                endTime = Date(),
                status = TaskStatus.IN_PROGRESS
            ),
            Task(
                id = "3",
                title = "Task 3",
                description = "Description for task 3",
                startTime = Date(),
                endTime = Date(),
                status = TaskStatus.COMPLETE
            )
        )
    }
}