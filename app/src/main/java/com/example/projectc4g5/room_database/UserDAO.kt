package com.example.projectc4g5.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDAO {
    @Query("SELECT*FROM User")
    suspend fun getAllUsers(): List<User>

    @Insert(onConflict=OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Insert(onConflict=OnConflictStrategy.IGNORE)
    suspend fun insertUsers(user: List<User>?): List<Long>

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}