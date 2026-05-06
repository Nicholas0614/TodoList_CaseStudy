package com.example.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.todolist.data.TodoDatabase
import com.example.todolist.models.TodoDetails
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    // 👇 KEEP your existing logic
    val selectedTodo = MutableLiveData<TodoDetails?>()

    // 👇 ADD Room
    private val dao = TodoDatabase.getDatabase(application).todoDao()

    val allTodos: LiveData<List<TodoDetails>> = dao.getAllTodos()

    fun insert(todo: TodoDetails) = viewModelScope.launch {
        dao.insert(todo)
    }

    fun update(todo: TodoDetails) = viewModelScope.launch {
        dao.update(todo)
    }

    fun delete(todo: TodoDetails) = viewModelScope.launch {
        dao.delete(todo)
    }

    fun refreshSelectedTodo(id: Int) {
        viewModelScope.launch {
            val updated = dao.getTodoById(id)

            // 🔥 force update even if same object
            selectedTodo.postValue(updated.copy())
        }
    }
}