package com.example.projectc4g5.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ToDoDAO {
    @Query("SELECT*FROM ToDo")
    suspend fun getAllTasks(): List<ToDo>

    @Insert(onConflict=OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: ToDo): Long

    @Insert(onConflict=OnConflictStrategy.IGNORE)
    suspend fun insertTasks(task: List<ToDo>?): List<Long>

    @Update
    suspend fun updateTask(task: ToDo)

    @Delete
    suspend fun deleteTask(task: ToDo)
}