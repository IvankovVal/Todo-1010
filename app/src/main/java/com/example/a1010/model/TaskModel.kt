package com.example.a1010.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class TaskModel (

    var name: String? = null,
    var status: Boolean = false,
    var id: Int? = null,

){
    companion object {
        fun parseFromJSONObject(json: JSONObject): TaskModel {
            val text = json.getString("text")
            val status = json.getBoolean("status")
            val id = json.getInt("id")
            return TaskModel(text, status, id)
        }
    }
}