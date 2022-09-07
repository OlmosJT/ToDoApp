package uz.gita.todoapp.presentation.viewmodel

import android.app.AlarmManager
import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import uz.gita.todoapp.data.local.entity.TaskEntity
import uz.gita.todoapp.data.local.model.TaskModel
import java.util.*

interface MainViewModel {
    val allTasksLiveData: LiveData<List<TaskModel>>
    val taskByIdLiveData: LiveData<TaskEntity>
    val errorLiveData: LiveData<String>
    val successLiveData: LiveData<Unit>
    val emptyLiveData: LiveData<Unit>
    val nonEmptyLiveData: LiveData<Unit>


    fun getAllTasks()

    fun getTaskById(id: Int)

    fun addTask(taskModel: TaskModel)

    fun deleteTaskById(id: Int)

    fun updateTask(taskEntity: TaskEntity)
}