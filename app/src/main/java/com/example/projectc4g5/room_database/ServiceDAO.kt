package com.example.projectc4g5.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ServiceDAO {
    @Query("SELECT*FROM Service")
    suspend fun getAllServices(): List<Service>

    @Insert(onConflict=OnConflictStrategy.IGNORE)
    suspend fun insertService(service: Service): Long

    @Insert(onConflict=OnConflictStrategy.IGNORE)
    suspend fun insertServices(service: List<Service>?): List<Long>

    @Update
    suspend fun updateService(service: Service)

    @Delete
    suspend fun deleteService(service: Service)
}