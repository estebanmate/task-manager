package com.example.taskmanager

import com.example.taskmanager.model.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseManager {

    private val db = Firebase.firestore
    private val tasksCollection = db.collection("tasks")

    fun getTasks(callback: (List<Task>) -> Unit) {
        tasksCollection.get()
            .addOnSuccessListener { result ->
                val tasks = result.map { document ->
                    document.toObject(Task::class.java)
                }
                callback(tasks)
            }
    }

    fun addTask(task: Task, callback: (Boolean) -> Unit) {
        tasksCollection.add(task)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun updateTask(task: Task, callback: (Boolean) -> Unit) {
        task.id?.let {
            tasksCollection.document(it).set(task)
                .addOnSuccessListener {
                    callback(true)
                }
                .addOnFailureListener {
                    callback(false)
                }
        }
    }
}