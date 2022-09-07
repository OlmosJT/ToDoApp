package uz.gita.todoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.gita.todoapp.data.local.model.TaskModel

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val date: String,
    val description: String,
    val isComplete: Boolean = false,
    val alarmTime: String,
    val event: String
)

fun TaskEntity.toTaskModel(): TaskModel {
    return TaskModel(
        id = id,
        title = title,
        date = date,
        description = description,
        isComplete = isComplete,
        time = alarmTime,
        event = event
    )
}