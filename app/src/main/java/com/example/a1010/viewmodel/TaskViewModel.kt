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
    val activeTasks: MutableLiveData<ArrayList<TaskModel>> by lazy { MutableLiveData<ArrayList<TaskModel>>() }
    val completeTasks: MutableLiveData<ArrayList<TaskModel>> by lazy { MutableLiveData<ArrayList<TaskModel>>() }
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
    //-------------Функция получения списка "В работе"-------------------------------------------
    fun getActiveTasks() {

        val callActiveTasks = ApiClient.instance?.api?.getMyActiveTask()
        callActiveTasks?.enqueue(object : Callback<ArrayList<TaskModel>> {
            override fun onResponse(
                call: Call<ArrayList<TaskModel>>,
                response: Response<ArrayList<TaskModel>>
            ) {
//-------------переменная со списком
                val loadTasks = response.body()
                activeTasks.postValue(loadTasks)
            }

            override fun onFailure(call: Call<ArrayList<TaskModel>>, t: Throwable) {
                // Toast.makeText(this@MainActivity, "ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT).show()

            }
        })

    }
    //-------------Функция получения списка "Готово"-------------------------------------------
    fun getCompliteTasks() {

        val callActiveTasks = ApiClient.instance?.api?.getMyCompleteTask()
        callActiveTasks?.enqueue(object : Callback<ArrayList<TaskModel>> {
            override fun onResponse(
                call: Call<ArrayList<TaskModel>>,
                response: Response<ArrayList<TaskModel>>
            ) {
//-------------переменная со списком
                val loadTasks = response.body()
                completeTasks.postValue(loadTasks)
            }

            override fun onFailure(call: Call<ArrayList<TaskModel>>, t: Throwable) {
                // Toast.makeText(this@MainActivity, "ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT).show()

            }
        })

    }
    //-------------Функция добавления задачи-------------------------------------------
    fun insert(name: String?, status: Int){
        viewModelScope.launch(Dispatchers.IO) {
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

    fun delete_task (id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            val callDeleteTask: Call<ResponseBody?>? = ApiClient.instance?.api?.deleteMyTask(id)

            callDeleteTask?.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                    //Toast.makeText(context,"Задача удалена",Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    // Toast.makeText( context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun update_task(id: Int, name: String?, status: Int?) {   // { //(task: TaskModel)
        val callUpdateCategory = ApiClient.instance?.api?.updateMyTask(id,name,status)

        callUpdateCategory?.enqueue(object : retrofit2.Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                //Toast.makeText(this,"Задача обновлена",Toast.LENGTH_SHORT).show()

               // loadScreen()

            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
               // Toast.makeText(context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!",Toast.LENGTH_SHORT).show()
            }
        })

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