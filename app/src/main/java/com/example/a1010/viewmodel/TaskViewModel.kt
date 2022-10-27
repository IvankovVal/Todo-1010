package com.example.a1010.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.a1010.model.ApiClient
import com.example.a1010.model.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskViewModel(application: Application): AndroidViewModel(application){
    val db: MutableLiveData<ArrayList<TaskModel>> by lazy { MutableLiveData<ArrayList<TaskModel>>() }
    init {
        getAllTasks()
    }

    //-------------Функция получения всех задач-------------------------------------------
    fun getAllTasks() {

        val callActiveTasks = ApiClient.instance?.api?.getAllMyTask()
        callActiveTasks?.enqueue(object : Callback<ArrayList<TaskModel>> {
            override fun onResponse(
                call: Call<ArrayList<TaskModel>>,
                response: Response<ArrayList<TaskModel>>
            ) {
//-------------переменная со списком
                val loadTasks = response.body()
                db.postValue(loadTasks)
            }

            override fun onFailure(call: Call<ArrayList<TaskModel>>, t: Throwable) {
                // Toast.makeText(this@MainActivity, "ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT).show()

            }
        })

    }

    fun insert(name: String?, status: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            //db.taskDao().insert(task)
            // tasks.value = tasks.value!!.plus(task)
            val callInsertTask: Call<ResponseBody?>? = ApiClient.instance?.api?.insertMyTask(name, status)
            callInsertTask?.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {

                    //Toast.makeText(this, "Задача добавлена", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {

                    //Toast.makeText(this, "ОШИБКА", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun delete_task (task: TaskModel){
        viewModelScope.launch(Dispatchers.IO) {
           // db.taskDao().delete_task(task)
        }
    }

    fun update_task (task: TaskModel){
        viewModelScope.launch(Dispatchers.IO) {
           // db.taskDao().update_task(task)
        }
    }

    fun onTaskCheckedChange(task: TaskModel, checked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
           // db.taskDao().update_task(task.copy(status = checked))
        }

    }


}
////мусор:
////Получение списка всех задач
//internal val allTasks  = db.value
////Получение списка выполненых задач
//internal val allCompleteTasks  = db.value //отфильтровать список со статусом true
////Получение списка невыполненных задач
//internal val allActiveTasks  = db.value //db.value //отфильтровать список со статусом false
//
//// private val tasks: MutableLiveData<List<Task>> = MutableLiveData(listOf())