package uz.gita.todoapp.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.todoapp.data.local.entity.TaskEntity
import uz.gita.todoapp.data.local.entity.toTaskModel
import uz.gita.todoapp.data.local.model.TaskModel
import uz.gita.todoapp.domain.repository.AppRepository
import uz.gita.todoapp.domain.usecase.AppUseCase
import javax.inject.Inject

class AppUseCaseImpl @Inject constructor(
    private val repository: AppRepository
): AppUseCase {
    override fun getAllTasks(): Flow<List<TaskModel>> = flow<List<TaskModel>> {
        val list = repository.getAllTasks().map {
            it.toTaskModel()
        }
        emit(list)
    }.flowOn(Dispatchers.IO)

    override fun getTaskById(id: Int) = flow<TaskEntity> {
        emit(repository.getTaskById(id))
    }.flowOn(Dispatchers.IO)

    override fun addTask(taskEntity: TaskEntity) = flow<Unit> {
        repository.addTask(taskEntity)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    override fun deleteTaskById(id: Int) = flow<Unit> {
        repository.deleteTaskById(id)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    override fun updateTask(taskEntity: TaskEntity) = flow<Unit> {
        repository.updateTask(taskEntity)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

}