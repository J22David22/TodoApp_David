package com.example.projectc4g5.room_database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey (autoGenerate = true) val id:Int,
    val username: String?,
    val names: String?,
    val lastnames: String?,
    val idNumber: String?,
    val email: String?,
    val phone: String?,
    val password: String?
): java.io.Serializable{

}