package com.example.a1010.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.a1010.database.RoomSingleton
import com.example.a1010.model.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application): AndroidViewModel(application){
    private val db: RoomSingleton = RoomSingleton.getInstance(application)
    //Получение списка всех задач
    internal val allTasks : LiveData<List<TaskModel>> = db.taskDao().allTasks()
    //Получение списка выполненых задач
    internal val allCompleteTasks : LiveData<List<TaskModel>> = db.taskDao().allCompleteTasks()
    //Получение списка невыполненных задач
    internal val allActiveTasks : LiveData<List<TaskModel>> = db.taskDao().allActiveTasks()

    // private val tasks: MutableLiveData<List<Task>> = MutableLiveData(listOf())

    fun insert(task: TaskModel){
        viewModelScope.launch(Dispatchers.IO) {
            db.taskDao().insert(task)
            // tasks.value = tasks.value!!.plus(task)
        }
    }

    fun delete_task (task: TaskModel){
        viewModelScope.launch(Dispatchers.IO) {
            db.taskDao().delete_task(task)
        }
    }

    fun update_task (task: TaskModel){
        viewModelScope.launch(Dispatchers.IO) {
            db.taskDao().update_task(task)
        }
    }

    fun onTaskCheckedChange(task: TaskModel, checked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            db.taskDao().update_task(task.copy(status = checked))
        }

    }


}