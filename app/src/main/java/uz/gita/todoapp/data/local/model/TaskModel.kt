package uz.gita.todoapp.data.local.model

import uz.gita.todoapp.data.local.entity.TaskEntity

data class TaskModel(
    val id: Int,
    val title: String,
    val date: String,
    val description: String,
    val isComplete: Boolean = false,
    val time: String,
    val event: String
) {
    fun toTaskEntity() = TaskEntity(
        id = id,
        title = title,
        date = date,
        description = description,
        isComplete = isComplete,
        alarmTime = time,
        event = event
    )
}
