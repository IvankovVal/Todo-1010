package com.example.a1010.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.a1010.model.ApiClient
import com.example.a1010.model.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class FilterType {
    ALL,
    COMPLETE,
    ACTIVE
}

class TaskViewModel(application: Application): AndroidViewModel(application){
    private val db: MutableLiveData<ArrayList<TaskModel>> by lazy { MutableLiveData<ArrayList<TaskModel>>() }
    private val filterType: MutableLiveData<FilterType> by lazy { MutableLiveData<FilterType>(FilterType.ALL) }
    val tasks: LiveData<ArrayList<TaskModel>> = db
    var counter:Int? = null

    fun setFilterType(to: FilterType) {
        filterType.value = to
        getAllTasks()
    }

    init {
        getAllTasks()
    }
    //-------------Функция получения всех задач-------------------------------------------
    fun getAllTasks() {
        val callActiveTasks: Call<ArrayList<TaskModel>>? = when (filterType.value) {
            FilterType.ALL -> ApiClient.instance?.api?.getAllMyTask()
            FilterType.COMPLETE -> ApiClient.instance?.api?.getMyCompleteTask()
            FilterType.ACTIVE -> ApiClient.instance?.api?.getMyActiveTask()
            else -> null
        }
        callActiveTasks?.enqueue(object : Callback<ArrayList<TaskModel>> {
            override fun onResponse(
                call: Call<ArrayList<TaskModel>>,
                response: Response<ArrayList<TaskModel>>
            ) {
//-------------переменная со списком
                val loadTasks = response.body()
                db.postValue(loadTasks)
                counter = loadTasks!!.size
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
    fun update_task(id: Int, name: String?, status: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
        val callUpdateCategory = ApiClient.instance?.api?.updateMyTask(id,name,status)

        callUpdateCategory?.enqueue(object : retrofit2.Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                //Toast.makeText(this,"Задача обновлена",Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
               // Toast.makeText(context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!",Toast.LENGTH_SHORT).show()
            }
        })
    }}
    fun onTaskCheckedChange(task: TaskModel, checked: Int) {
        viewModelScope.launch(Dispatchers.IO) {

                val callUpdateCategory = ApiClient.instance?.api?.updateMyTask(task.id!!,task.name,checked)

                callUpdateCategory?.enqueue(object : retrofit2.Callback<ResponseBody?> {
                    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                        //Toast.makeText(this,"Задача обновлена",Toast.LENGTH_SHORT).show()
                    }
                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        // Toast.makeText(context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!",Toast.LENGTH_SHORT).show()
                    }
                })
            }

    }

    }



