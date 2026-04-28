package com.example.todolist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.models.TodoDetails

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(todo: TodoDetails)

    @Update
    suspend fun update(todo: TodoDetails)

    @Delete
    suspend fun delete(todo: TodoDetails)

    @Query("SELECT * FROM todo_table ORDER BY date DESC")
    fun getAllTodos(): LiveData<List<TodoDetails>>

    @Query("SELECT * FROM todo_table WHERE id = :id")
    suspend fun getTodoById(id: Int): TodoDetails
}