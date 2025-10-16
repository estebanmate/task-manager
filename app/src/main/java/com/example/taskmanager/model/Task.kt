package com.example.taskmanager.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class Task(
    @DocumentId
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val startTime: Date = Date(),
    val endTime: Date = Date(),
    val status: TaskStatus = TaskStatus.TO_DO
)