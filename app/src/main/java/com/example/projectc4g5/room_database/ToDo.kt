package com.example.projectc4g5.room_database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDo(
    @PrimaryKey (autoGenerate = true) val id:Int,
    val title: String?,
    val time: String?,
    val place: String?
): java.io.Serializable{

}
