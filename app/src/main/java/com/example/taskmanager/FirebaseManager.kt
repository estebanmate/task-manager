package com.example.taskmanager

import com.example.taskmanager.model.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseManager {

    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val tasksCollection = db.collection("tasks")

    fun registerUser(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    fun loginUser(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    fun getCurrentUser() = auth.currentUser

    fun getTasks(callback: (List<Task>) -> Unit) {
        val userId = getCurrentUser()?.uid ?: return
        tasksCollection.whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val tasks = result.map { document ->
                    val task = document.toObject(Task::class.java)
                    task.id = document.id
                    task
                }
                callback(tasks)
            }
    }

    fun addTask(task: Task, callback: (Boolean) -> Unit) {
        val userId = getCurrentUser()?.uid
        if (userId != null) {
            task.userId = userId
            tasksCollection.add(task)
                .addOnSuccessListener {
                    callback(true)
                }
                .addOnFailureListener {
                    callback(false)
                }
        } else {
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