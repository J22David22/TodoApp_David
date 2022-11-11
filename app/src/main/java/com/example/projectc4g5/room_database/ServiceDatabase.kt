package com.example.projectc4g5.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Service::class), version =1)
abstract class ServiceDatabase: RoomDatabase() {
    abstract fun serviceDao():ServiceDAO

    companion object{
        @Volatile
        private var INSTANCE: ServiceDatabase? = null

        fun getDatabase(context: Context):ServiceDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                ServiceDatabase::class.java,
                "ServiceDatabase").build()
                INSTANCE=instance
                instance
            }
        }
    }
}