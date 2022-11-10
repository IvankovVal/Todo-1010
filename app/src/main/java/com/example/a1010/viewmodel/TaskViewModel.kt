package com.example.a1010.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.a1010.model.ApiClient
import com.example.a1010.model.TaskModel
import com.example.a1010.view.RecyclerViewAdapter
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

enum class FilterType {
    ALL,
    COMPLETE,
    ACTIVE
}

class TaskViewModel(application: Application): AndroidViewModel(application){
   val db: MutableLiveData<ArrayList<TaskModel>> by lazy { MutableLiveData<ArrayList<TaskModel>>() }
    private val filterType: MutableLiveData<FilterType> by lazy { MutableLiveData<FilterType>(FilterType.ALL) }
    val tasks: LiveData<ArrayList<TaskModel>> = db


//    lateinit var counterActive:ArrayList<TaskModel>
//    lateinit var counterAll:ArrayList<TaskModel>
//    lateinit var counterComplete:ArrayList<TaskModel>

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
//                db.value = loadTasks
                db.postValue(loadTasks)
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
                    getAllTasks()
                    //Toast.makeText(this, "Задача добавлена", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {

                    //Toast.makeText(this, "ОШИБКА", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
    //--------------Удаление задачи------------------------------------------------------
    fun delete_task (id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            val callDeleteTask: Call<ResponseBody?>? = ApiClient.instance?.api?.deleteMyTask(id)

            callDeleteTask?.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                    //Toast.makeText(context,"Задача удалена",Toast.LENGTH_SHORT).show()
                    getAllTasks()
                }
                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    // Toast.makeText( context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    //--------------Все готовы------------------------------------------------------
    fun setAllComplete() {
        viewModelScope.launch(Dispatchers.IO) {
            val callUpdateCategory = ApiClient.instance?.api?.setAllComplete()

            callUpdateCategory?.enqueue(object : retrofit2.Callback<ResponseBody?> {
                override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                    //Toast.makeText(this,"Задача обновлена",Toast.LENGTH_SHORT).show()
                    getAllTasks()
                }
                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    // Toast.makeText(context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!",Toast.LENGTH_SHORT).show()
                }
            })
        }}
    //--------------Редактирование задачи------------------------------------------------------
    fun update_task(id: Int, name: String?, status: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
        val callUpdateCategory = ApiClient.instance?.api?.updateMyTask(id,name,status)

        callUpdateCategory?.enqueue(object : retrofit2.Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                //Toast.makeText(this,"Задача обновлена",Toast.LENGTH_SHORT).show()
                getAllTasks()
            }
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
               // Toast.makeText(context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!",Toast.LENGTH_SHORT).show()
            }
        })
    }}
    //--------------Смена статуса задач------------------------------------------------------
    fun onTaskCheckedChange(task: TaskModel, checked: Int) {
        viewModelScope.launch(Dispatchers.IO) {

                val callUpdateCategory = ApiClient.instance?.api?.updateMyTask(task.id!!,task.name,checked)

                callUpdateCategory?.enqueue(object : retrofit2.Callback<ResponseBody?> {
                    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                        //Toast.makeText(this,"Задача обновлена",Toast.LENGTH_SHORT).show()
                        getAllTasks()
                    }
                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        // Toast.makeText(context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!",Toast.LENGTH_SHORT).show()
                    }
                })
            }}
    //--------------Удаление выполненных задач------------------------------------------------------
        fun delComTasks(){
            viewModelScope.launch(Dispatchers.IO) {
            val callDeleteComplete: Call<ResponseBody?>? = ApiClient.instance?.api?.delComTask()

            callDeleteComplete?.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>, response: Response<ResponseBody?>
                ) {
                    getAllTasks()
//                    Toast.makeText(this@MainActivity, "Задачи удалены", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!",
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            })
        }

    }
    fun insertWithNative(name: String?, status: Int) {
        // create HTTPUrlConnection
        // parse TaskModel into json string
        // configure HTTPUrlConnection to send data
        // parse output???
        // update UI
    }
    suspend fun feedMyCat() {
        TODO("Not yet implemented")
        //Запостить на сервер задачу "Покормить кота"
        postFeedCat()
        //Получить задачу
       // getMethod()
        //Распарсить
        //Добавить в список задач
    }
//https://api.beget.com/api/mysql/getList?login=bellat3u&passwd=LY3knSLm&output_format=json  -  запросы к таблице (здесь просим лист - метод getList)
    private suspend fun postFeedCat() {
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("id", "some id")
        jsonObject.put("name", "Покорми кота")
        jsonObject.put("status", "0")
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val url = URL("http://bellat3u.beget.tech/insertTask.php")
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "POST"
        httpURLConnection.setRequestProperty("Content-Type", "application/json") // The format of the content we're sending to the server
        httpURLConnection.setRequestProperty("Accept", "application/json") // The format of response we want to get from the server
        httpURLConnection.doInput = true
        httpURLConnection.doOutput = true

            // Send the JSON we created
        val outputStreamWriter = OutputStreamWriter(httpURLConnection.outputStream)
        outputStreamWriter.write(jsonObjectString)
        outputStreamWriter.flush()

        // Check if the connection is successful
        val responseCode = httpURLConnection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val response = httpURLConnection.inputStream.bufferedReader()
                .use { it.readText() }  // defaults to UTF-8
            withContext(Dispatchers.IO) {

                // Convert raw JSON to pretty JSON using GSON library
                val gson = GsonBuilder().setPrettyPrinting().create()
                val myJson = gson.toJson(JsonParser.parseString(response))
                Log.d("My Printed JSON :", myJson)

            }
        } else {
            Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
        }
    }
    fun getMethod() {
        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("http://bellat3u.beget.tech")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.setRequestProperty("Accept", "application/json") // The format of response we want to get from the server
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = false
            // Check if the connection is successful
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = httpURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val myJson = gson.toJson(JsonParser.parseString(response))
                    Log.d("My Printed JSON :", myJson)

                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }
    }
    }






