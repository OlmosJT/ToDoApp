package uz.gita.todoapp.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.gita.todoapp.data.local.entity.TaskEntity
import uz.gita.todoapp.data.local.model.TaskModel

interface AppUseCase {
    fun getAllTasks(): Flow<List<TaskModel>>

    fun getTaskById(id: Int): Flow<TaskEntity>

    fun addTask(taskEntity: TaskEntity): Flow<Unit>

    fun deleteTaskById(id: Int): Flow<Unit>

    fun updateTask(taskEntity: TaskEntity): Flow<Unit>
}