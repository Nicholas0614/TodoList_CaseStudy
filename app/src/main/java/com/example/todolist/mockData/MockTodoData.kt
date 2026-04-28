package com.example.todolist.mockDatas

import com.example.todolist.models.ToDoStatus
import com.example.todolist.models.TodoDetails

open class MockTodoData {

    open fun populateData(): List<TodoDetails> = listOf(

        TodoDetails(
            id = 2,
            title = "Task 2",
            details = "Details for task 2",
            meaning = "This task is about completing step 2 of the workflow",
            synonyms = "job, assignment, work",
            status = ToDoStatus.NEW,
            date = System.currentTimeMillis()
        ),

        TodoDetails(
            id = 3,
            title = "Task 3",
            details = "Details for task 3",
            meaning = "This task involves reviewing previous work",
            synonyms = "review, check, inspect",
            status = ToDoStatus.DONE,
            date = System.currentTimeMillis()
        ),

        TodoDetails(
            id = 4,
            title = "Task 4",
            details = "Details for task 4",
            meaning = "This task focuses on planning next steps",
            synonyms = "plan, prepare, organize",
            status = ToDoStatus.NEW,
            date = System.currentTimeMillis()
        ),

        TodoDetails(
            id = 5,
            title = "Task 5",
            details = "Details for task 5",
            meaning = "This task is about testing features",
            synonyms = "test, verify, validate",
            status = ToDoStatus.DONE,
            date = System.currentTimeMillis()
        ),

        TodoDetails(
            id = 6,
            title = "Task 6",
            details = "Details for task 6",
            meaning = "This task is related to UI design improvements",
            synonyms = "design, UI, interface",
            status = ToDoStatus.NEW,
            date = System.currentTimeMillis()
        ),

        TodoDetails(
            id = 7,
            title = "Task 7",
            details = "Details for task 7",
            meaning = "This task is about fixing bugs in the system",
            synonyms = "bugfix, repair, fix",
            status = ToDoStatus.NEW,
            date = System.currentTimeMillis()
        ),

        TodoDetails(
            id = 8,
            title = "Task 8",
            details = "Details for task 8",
            meaning = "This task is about backend integration",
            synonyms = "backend, API, server",
            status = ToDoStatus.DONE,
            date = System.currentTimeMillis()
        ),

        TodoDetails(
            id = 9,
            title = "Task 9",
            details = "Details for task 9",
            meaning = "This task is about documentation writing",
            synonyms = "docs, writing, notes",
            status = ToDoStatus.NEW,
            date = System.currentTimeMillis()
        ),

        TodoDetails(
            id = 10,
            title = "Task 10",
            details = "Details for task 10",
            meaning = "This task is final project cleanup",
            synonyms = "cleanup, finalize, polish",
            status = ToDoStatus.NEW,
            date = System.currentTimeMillis()
        )
    )
}