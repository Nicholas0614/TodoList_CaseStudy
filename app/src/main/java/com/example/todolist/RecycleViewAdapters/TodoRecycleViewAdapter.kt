package com.example.todolist.RecycleViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.models.TodoDetails

class TodoRecycleViewAdapter(
    private val onItemClick: (TodoDetails) -> Unit
) : ListAdapter<TodoDetails, TodoRecycleViewAdapter.TodoViewHolder>(DiffCallback) {

    companion object {

        val DiffCallback = object : DiffUtil.ItemCallback<TodoDetails>() {

            override fun areItemsTheSame(oldItem: TodoDetails, newItem: TodoDetails): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TodoDetails, newItem: TodoDetails): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val detailTextView: TextView = itemView.findViewById(R.id.textViewDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)

        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {

        val todo = getItem(position)

        holder.titleTextView.text = todo.title
        holder.detailTextView.text = todo.details

        holder.itemView.setOnClickListener {
            onItemClick(todo)
        }
    }
}