package uz.gita.todoapp.presentation.viewmodel.impl

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.gita.todoapp.data.local.entity.TaskEntity
import uz.gita.todoapp.data.local.model.TaskModel
import uz.gita.todoapp.domain.usecase.AppUseCase
import uz.gita.todoapp.presentation.ui.broadcast.AlarmReceiver
import uz.gita.todoapp.presentation.viewmodel.MainViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModelImpl @Inject constructor(
    private val useCase: AppUseCase
): ViewModel(), MainViewModel {
    override val allTasksLiveData = MutableLiveData<List<TaskModel>>()
    override val taskByIdLiveData = MutableLiveData<TaskEntity>()
    override val errorLiveData = MutableLiveData<String>()
    override val successLiveData = MutableLiveData<Unit>()
    override val emptyLiveData = MutableLiveData<Unit>()
    override val nonEmptyLiveData = MutableLiveData<Unit>()

    init {
        getAllTasks()
    }

    override fun getAllTasks() {
        useCase.getAllTasks().onEach {
            Log.d("TTT", "List: " +it.size.toString())
            if(it.isEmpty()){
                emptyLiveData.value = Unit
            } else {
                nonEmptyLiveData.value = Unit
            }
            allTasksLiveData.value = it
        }.launchIn(viewModelScope)
    }

    override fun getTaskById(id: Int){
        useCase.getTaskById(id).onEach {
            taskByIdLiveData.value = it
        }.launchIn(viewModelScope)
    }

    override fun addTask(taskModel: TaskModel) {
        useCase.addTask(taskModel.toTaskEntity()).onEach {
            successLiveData.value = Unit
        }.launchIn(viewModelScope)
    }

    override fun deleteTaskById(id: Int) {
        useCase.deleteTaskById(id).onEach {
            successLiveData.value  = Unit
        }.launchIn(viewModelScope)
    }

    override fun updateTask(taskEntity: TaskEntity) {
        useCase.updateTask(taskEntity).onEach {
            successLiveData.value = Unit
        }.launchIn(viewModelScope)
    }

}