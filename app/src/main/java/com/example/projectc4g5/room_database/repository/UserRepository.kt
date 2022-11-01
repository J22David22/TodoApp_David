package com.example.projectc4g5.room_database.repository

import com.example.projectc4g5.room_database.User
import com.example.projectc4g5.room_database.UserDAO

class UserRepository (val userDao: UserDAO){

    suspend fun getAllUsers(): List<User>{
        return userDao.getAllUsers()
    }

    suspend fun insertUser(user: User): Long{
        return userDao.insertUser(user)
    }

    suspend fun deleteUser(user: User){
        return userDao.deleteUser(user)
    }

    suspend fun insertUsers(user: List<User>?): List<Long>{
        return userDao.insertUsers(user)
    }
}