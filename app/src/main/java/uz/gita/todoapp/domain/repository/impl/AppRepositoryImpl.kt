package uz.gita.todoapp.domain.repository.impl

import uz.gita.todoapp.data.local.dao.TasksDao
import uz.gita.todoapp.data.local.entity.TaskEntity
import uz.gita.todoapp.domain.repository.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val tasksDao: TasksDao
): AppRepository {
    override suspend fun getAllTasks(): List<TaskEntity> = tasksDao.getAllTasks()

    override suspend fun addTask(taskEntity: TaskEntity) {
        tasksDao.addTask(taskEntity)
    }

    override suspend fun deleteTaskById(id: Int) {
        tasksDao.deleteTaskById(id)
    }

    override suspend fun getTaskById(id: Int): TaskEntity = tasksDao.getTaskById(id)

    override suspend fun updateTask(taskEntity: TaskEntity) {
        tasksDao.updateTask(taskEntity)
    }
}