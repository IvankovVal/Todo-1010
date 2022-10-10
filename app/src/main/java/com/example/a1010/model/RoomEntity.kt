package com.example.a1010.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey
    var id:Long?,

    @ColumnInfo(name = "uuid")
    var name: String,

    @ColumnInfo(name = "status")
    var status: Boolean
)