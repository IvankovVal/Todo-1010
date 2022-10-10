package com.example.a1010.viewmodel

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.a1010.model.Task

@Dao
interface TaskDao{
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun allTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Delete()
    fun delete_task(task: Task)

    @Update()
    fun update_task(task: Task)

}