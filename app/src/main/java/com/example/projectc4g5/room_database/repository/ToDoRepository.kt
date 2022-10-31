package com.example.projectc4g5.room_database.repository

import com.example.projectc4g5.room_database.ToDo
import com.example.projectc4g5.room_database.ToDoDAO

class ToDoRepository (val toDoDao: ToDoDAO){

    suspend fun getAllTasks(): List<ToDo>{
        return toDoDao.getAllTasks()
    }

    suspend fun insertTask(toDo: ToDo): Long{
        return toDoDao.insertTask(toDo)
    }

    suspend fun insertTasks(toDo: List<ToDo>?): List<Long>{
        return toDoDao.insertTasks(toDo)
    }
}