package com.example.projectc4g5.room_database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectc4g5.room_database.User
import com.example.projectc4g5.room_database.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserViewModel (private val repository: UserRepository): ViewModel() {

    var users: List<User>? = null

    fun getAllUsers(): Job {
        return viewModelScope.async {
            users = repository.getAllUsers()
        }
    }

    fun getTheUsers(): List<User>? {
        return users
    }

    fun insertUser(user: User):Long{
        var idUser: Long = 0
        viewModelScope.launch {
            idUser=repository.insertUser(user)
        }
        return idUser
    }

    fun insertUsers(user: List<User>?):List<Long>?{
        var idUser: List<Long>? = null
        viewModelScope.launch {
            idUser=repository.insertUsers(user)
        }
        return idUser
    }

}