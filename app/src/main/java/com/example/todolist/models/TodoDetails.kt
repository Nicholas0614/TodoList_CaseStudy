package com.example.todolist.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "todo_table")
data class TodoDetails (

    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,   // 👈 changed from Double → Int for Room

    val title : String,
    val meaning : String? = null,
    val synonyms : String? = null,
    val details : String,
    val status : ToDoStatus,
    val date : Long
) : Parcelable

enum class ToDoStatus {
    NEW,
    DONE
}