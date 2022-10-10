package com.example.a1010.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.a1010.database.RoomSingleton
import com.example.a1010.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application): AndroidViewModel(application){
    private val db: RoomSingleton = RoomSingleton.getInstance(application)
    internal val allTasks : LiveData<List<Task>> = db.taskDao().allTasks()

    // private val tasks: MutableLiveData<List<Task>> = MutableLiveData(listOf())

    fun insert(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            db.taskDao().insert(task)
            // tasks.value = tasks.value!!.plus(task)
        }
    }

    fun delete_task (task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            db.taskDao().delete_task(task)
        }
    }

    fun update_task (task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            db.taskDao().update_task(task)
        }
    }

    fun onTaskCheckedChange(task: Task, checked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            db.taskDao().update_task(task.copy(status = checked))
        }

    }
}