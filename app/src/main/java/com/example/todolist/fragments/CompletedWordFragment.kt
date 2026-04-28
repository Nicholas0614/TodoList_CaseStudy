package com.example.todolist.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.RecycleViewAdapters.TodoRecycleViewAdapter
import com.example.todolist.models.TodoDetails
import com.example.todolist.viewmodel.TodoViewModel

class CompletedWordFragment : Fragment(R.layout.completed_word_fragment) {

    private lateinit var viewModel: TodoViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleView)
        val emptyView = view.findViewById<View>(R.id.emptyView)
        val sortButton = view.findViewById<ImageButton>(R.id.imageView)
        val addButton = view.findViewById<View>(R.id.button)

        viewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Adapter
        val adapter = TodoRecycleViewAdapter { todo ->

            viewModel.selectedTodo.value = todo

            TodoDetailsFragment().show(
                parentFragmentManager,
                "todo_details"
            )
        }

        recyclerView.adapter = adapter

        // ✅ base list (from Room)
        var baseList = listOf<TodoDetails>()

        // ✅ Empty state helper
        fun updateEmptyState(list: List<TodoDetails>) {
            if (list.isEmpty()) {
                emptyView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        // 🔥 ROOM OBSERVER (replaces mock data)
        viewModel.allTodos.observe(viewLifecycleOwner) { data ->

            baseList = data.filter { it.status.name == "DONE" }

            adapter.submitList(baseList)
            updateEmptyState(baseList)
        }

        // 📊 SORT
        sortButton.setOnClickListener {

            val dialogView = layoutInflater.inflate(R.layout.dialog_sort, null)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()

            val radioAsc = dialogView.findViewById<android.widget.RadioButton>(R.id.radioAsc)
            val radioDesc = dialogView.findViewById<android.widget.RadioButton>(R.id.radioDesc)
            val radioTitle = dialogView.findViewById<android.widget.RadioButton>(R.id.radioTitle)
            val radioDate = dialogView.findViewById<android.widget.RadioButton>(R.id.radioDate)
            val btnDone = dialogView.findViewById<View>(R.id.btnDone)

            btnDone.setOnClickListener {

                var sortedList = when {
                    radioTitle.isChecked -> baseList.sortedBy { it.title }
                    radioDate.isChecked -> baseList.sortedBy { it.date }
                    else -> baseList
                }

                if (radioDesc.isChecked) {
                    sortedList = sortedList.reversed()
                }

                adapter.submitList(sortedList)
                updateEmptyState(sortedList)

                dialog.dismiss()
            }
        }

        // ➕ ADD TODO
        addButton.setOnClickListener {
            AddTodoDialogFragment().show(parentFragmentManager, "add_todo")
        }
    }
}