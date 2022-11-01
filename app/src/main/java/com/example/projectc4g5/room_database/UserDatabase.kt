package com.example.projectc4g5.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.time.Instant

@Database(entities = arrayOf(User::class), version =1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao():UserDAO

    companion object{
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context):UserDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                UserDatabase::class.java,
                "UserDatabase").build()
                INSTANCE=instance
                instance
            }
        }
    }
}