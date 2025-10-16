package com.example.taskmanager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.taskmanager.model.TaskStatus
import java.text.SimpleDateFormat
import java.util.*

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var startTimeButton: Button
    private lateinit var endTimeButton: Button
    private lateinit var statusSpinner: Spinner
    private lateinit var saveButton: Button

    private var startTime = Calendar.getInstance()
    private var endTime = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        titleEditText = findViewById(R.id.titleEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        startTimeButton = findViewById(R.id.startTimeButton)
        endTimeButton = findViewById(R.id.endTimeButton)
        statusSpinner = findViewById(R.id.statusSpinner)
        saveButton = findViewById(R.id.saveButton)

        val statusAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            TaskStatus.values().map { it.name }
        )
        statusSpinner.adapter = statusAdapter

        startTimeButton.setOnClickListener {
            showDateTimePicker(startTime) {
                updateButtonText(startTimeButton, it)
            }
        }

        endTimeButton.setOnClickListener {
            showDateTimePicker(endTime) {
                updateButtonText(endTimeButton, it)
            }
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val status = TaskStatus.valueOf(statusSpinner.selectedItem.toString())

            val task = Task(
                title = title,
                description = description,
                startTime = startTime.time,
                endTime = endTime.time,
                status = status
            )

            FirebaseManager.addTask(task) { success ->
                if (success) {
                    finish()
                }
            }
        }
    }

    private fun showDateTimePicker(calendar: Calendar, onDateTimeSet: (Calendar) -> Unit) {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                        onDateTimeSet(calendar)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateButtonText(button: Button, calendar: Calendar) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        button.text = sdf.format(calendar.time)
    }
}