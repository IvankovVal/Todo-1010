package com.example.a1010.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a1010.model.Task
import com.example.a1010.viewmodel.TaskDao

@Database(entities = arrayOf(Task::class), version = 1, exportSchema = false)
abstract class RoomSingleton : RoomDatabase(){
    abstract fun taskDao(): TaskDao

    companion object{
        private var INSTANCE: RoomSingleton? = null
        fun getInstance(context: Context): RoomSingleton {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    RoomSingleton::class.java,
                    "roomdb")
                    .build()
            }

            return INSTANCE as RoomSingleton
        }
    }
}