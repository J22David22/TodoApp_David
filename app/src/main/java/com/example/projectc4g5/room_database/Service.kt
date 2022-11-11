package com.example.projectc4g5.room_database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Service(
    @PrimaryKey (autoGenerate = true) val id:Int,
    val nombre: String?,
    val encargado: String?,
    val precio: String?,
    val imagen: String?
): java.io.Serializable{

}