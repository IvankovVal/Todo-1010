package com.example.a1010.model

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    //Добавление задачи
    @FormUrlEncoded
    @POST("insertTask.php")
    fun insertMyTask(
        @Field("name") name: String?,
        @Field("status") status: Boolean?
    ): Call<ResponseBody?>?


    //Редактирование задачи
    @FormUrlEncoded
    @POST("updateTask.php")
    fun updateMyTask(
        @Field("id") id: Int,
        @Field("name") name: String?,
        @Field("status") status: Boolean?
    ): Call<ResponseBody?>?

    //Удаление задачи
    @FormUrlEncoded
    @POST("deleteTask.php")
    fun deleteMyTask(
        @Field("id") id: Int?
    ): Call<ResponseBody?>?


    //Запрос списка "все"
    @GET("getAllTasks.php")
    fun getAllMyTask(): Call<ArrayList<TaskModel>>

    //Запрос списка "готово"
    @GET("getMyCompleteTask.php")
    fun getMyCompleteTask(): Call<ArrayList<TaskModel>>

    //Запрос списка "в работе"
    @GET("getActiveTask.php")
    fun getMyActiveTask(): Call<ArrayList<TaskModel>>



}