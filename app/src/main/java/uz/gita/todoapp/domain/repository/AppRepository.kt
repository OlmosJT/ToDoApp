package uz.gita.todoapp.domain.repository

import androidx.room.Query
import androidx.room.Update
import uz.gita.todoapp.data.local.entity.TaskEntity

interface AppRepository {
    suspend fun getAllTasks(): List<TaskEntity>

    suspend fun getTaskById(id: Int): TaskEntity

    suspend fun addTask(taskEntity: TaskEntity)

    suspend fun deleteTaskById(id: Int)

    suspend fun updateTask(taskEntity: TaskEntity)
}