package com.example.a1010.viewmodel

import android.app.Application
import android.os.Handler
import androidx.lifecycle.*
import com.example.a1010.model.TaskModel
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

enum class FilterType {ALL,COMPLETE,ACTIVE}

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val db: MutableLiveData<ArrayList<TaskModel>> by lazy { MutableLiveData<ArrayList<TaskModel>>() }
    private val filterType: MutableLiveData<FilterType> by lazy {
        MutableLiveData<FilterType>(
            FilterType.ALL
        )
    }
    val tasks: LiveData<ArrayList<TaskModel>> = db
    private var page = 1
    var allCount:Int = 0
    var activeCount:Int = 0
    var completeCount:Int = 0


    fun setFilterType(to: FilterType) { filterType.value = to
    getAllTasks()}

    init { getAllTasks() }

    //-------------Функция получения всех задач-------------------------------------------
    private fun getAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            //объект URL
            val url = URL("https://news-feed.dunice-testing.com/api/v1/todo?page=$page&perPage=8")
            //создаём соединение вызывая метод объекта URL
            val httpsURLConnection = url.openConnection() as HttpsURLConnection
            httpsURLConnection.requestMethod = "GET"//метод запроса
            httpsURLConnection.setRequestProperty("Accept", "application/json")//тип данных
            httpsURLConnection.doInput = true//собираемся получать
            httpsURLConnection.doOutput = false

            val responseCode = httpsURLConnection.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val response = httpsURLConnection.inputStream.bufferedReader()//отклик
                    .use { it.readText() }
                withContext(Dispatchers.IO) {
                    val jsonArray = JSONTokener(response).nextValue() as JSONObject
                    val data = jsonArray.getJSONObject("data")
                    val contentArray = data.getJSONArray("content")
                    val taskList: ArrayList <TaskModel> = ArrayList()
                    //парсим ответ в список
                    for (i in 0 until contentArray.length()) {
                        val task = contentArray.getJSONObject(i)
                        taskList.add (TaskModel.parseFromJSONObject(task))
                    }
                    //-------------переменная со списком
                    val loadTasks: ArrayList<TaskModel>? = when (filterType.value) {
                        FilterType.ALL -> taskList
                        FilterType.COMPLETE -> taskList.filter { it.status == true } as ArrayList<TaskModel>
                        FilterType.ACTIVE -> taskList.filter { it.status == false } as ArrayList<TaskModel>
                        else -> null}
                     allCount = data.getInt("numberOfElements")
                    activeCount = data.getInt("notReady")
                    completeCount = data.getInt("ready")

//                    if (data.getInt("numberOfElements") == adapter.itemCount) {
//                        page = 0
//                        taskList = emptyList()
//                        Handler().postDelayed({
//                            getAllTasks()
//                        }, 100)
//                    }
//                    if (data.getInt("numberOfElements") > adapter.itemCount) {
//                        taskList = itemsArray
//                        tasks.value = tasks.value?.plus(itemsArray)
//                        page += 1
//                    }
//
//            else {
//            }
                        db.postValue(loadTasks)
                    } } }}
        //-------------Функция добавления задачи-------------------------------------------
        fun insert(name: String) {
            viewModelScope.launch {
                val jsonObject = JSONObject()
                jsonObject.put("text", name)
                val jsonObjectString = jsonObject.toString()

                GlobalScope.launch(Dispatchers.IO) {
                    val url = URL("https://news-feed.dunice-testing.com/api/v1/todo")
                    val httpsURLConnection = url.openConnection() as HttpsURLConnection
                    httpsURLConnection.requestMethod = "POST"
                    httpsURLConnection.setRequestProperty("Content-Type", "application/json")
                    httpsURLConnection.setRequestProperty("Accept", "application/json")
                    httpsURLConnection.doInput = true
                    httpsURLConnection.doOutput = true

                    val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                    outputStreamWriter.write(jsonObjectString)
                    outputStreamWriter.flush()

                    val responseCode = httpsURLConnection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                    } else {
                    }
                }
            }
        }
        //--------------Удаление задачи------------------------------------------------------
        fun delete_task(id: Int) {
            GlobalScope.launch(Dispatchers.IO) {
                val url = URL("https://news-feed.dunice-testing.com/api/v1/todo/$id")
                val httpsURLConnection = url.openConnection() as HttpsURLConnection
                httpsURLConnection.requestMethod = "DELETE"
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = false
                val responseCode = httpsURLConnection.responseCode
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                } else {
                }} }
      //--------------Редактирование задачи------------------------------------------------------
//    https://news-feed.dunice-testing.com/api/v1/todo/status/1
        fun update_task(id: Int, name: String?) {
            val jsonObject = JSONObject()
            jsonObject.put("text", name)
            val jsonObjectString = jsonObject.toString()

            GlobalScope.launch(Dispatchers.IO) {
                val url = URL("https://news-feed.dunice-testing.com/api/v1/todo/text/$id")
                val httpsURLConnection = url.openConnection() as HttpsURLConnection
                httpsURLConnection.requestMethod = "PATCH"
                httpsURLConnection.setRequestProperty("Content-Type", "application/json")
                httpsURLConnection.setRequestProperty("Accept", "application/json")
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = true

                val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                outputStreamWriter.write(jsonObjectString)
                outputStreamWriter.flush()

                val responseCode = httpsURLConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                } else {
                } } }
        //--------------Смена статуса задач------------------------------------------------------
        fun onTaskCheckedChange(id: Int?, checked: Boolean) {
            val jsonObject = JSONObject()
            jsonObject.put("status", checked)
            val jsonObjectString = jsonObject.toString()

            GlobalScope.launch(Dispatchers.IO) {
                val url = URL("https://news-feed.dunice-testing.com/api/v1/todo/status/$id")
                val httpsURLConnection = url.openConnection() as HttpsURLConnection
                httpsURLConnection.requestMethod = "PATCH"
                httpsURLConnection.setRequestProperty("Content-Type", "application/json")
                httpsURLConnection.setRequestProperty("Accept", "application/json")
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = true

                val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                outputStreamWriter.write(jsonObjectString)
                outputStreamWriter.flush()

                val responseCode = httpsURLConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                } else {
                }}}
        //--------------Удаление выполненных задач------------------------------------------------------
        fun delComTasks() {
            GlobalScope.launch(Dispatchers.IO) {
                val url = URL("https://news-feed.dunice-testing.com/api/v1/todo")
                val httpsURLConnection = url.openConnection() as HttpsURLConnection
                httpsURLConnection.requestMethod = "DELETE"
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = false
                val responseCode = httpsURLConnection.responseCode
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                } else {
                } } }
    }






