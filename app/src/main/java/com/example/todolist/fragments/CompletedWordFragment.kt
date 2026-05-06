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

    // ✅ SORT STATE (ADDED)
    private var sortBy = "TITLE"   // TITLE or DATE
    private var sortOrder = "ASC"  // ASC or DESC

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleView)
        val emptyView = view.findViewById<View>(R.id.emptyView)
        val sortButton = view.findViewById<ImageButton>(R.id.imageView)

        viewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = TodoRecycleViewAdapter { todo ->

            viewModel.selectedTodo.value = todo

            TodoDetailsFragment().show(
                parentFragmentManager,
                "todo_details"
            )
        }

        recyclerView.adapter = adapter

        var baseList = listOf<TodoDetails>()

        fun updateEmptyState(list: List<TodoDetails>) {
            if (list.isEmpty()) {
                emptyView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        // ✅ APPLY SORT (ADDED)
        fun applySort(list: List<TodoDetails>): List<TodoDetails> {
            var sortedList = when (sortBy) {
                "TITLE" -> list.sortedBy { it.title }
                "DATE" -> list.sortedBy { it.date }
                else -> list
            }

            if (sortOrder == "DESC") {
                sortedList = sortedList.reversed()
            }

            return sortedList
        }

        // 🔥 ROOM OBSERVER
        viewModel.allTodos.observe(viewLifecycleOwner) { data ->

            baseList = data.filter { it.status.name == "DONE" }

            val sortedList = applySort(baseList)

            adapter.submitList(sortedList)
            updateEmptyState(sortedList)
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

            // ✅ RESTORE SELECTION
            radioTitle.isChecked = sortBy == "TITLE"
            radioDate.isChecked = sortBy == "DATE"
            radioAsc.isChecked = sortOrder == "ASC"
            radioDesc.isChecked = sortOrder == "DESC"

            btnDone.setOnClickListener {

                // ✅ SAVE SELECTION
                sortBy = when {
                    radioTitle.isChecked -> "TITLE"
                    radioDate.isChecked -> "DATE"
                    else -> sortBy
                }

                sortOrder = if (radioDesc.isChecked) "DESC" else "ASC"

                val sortedList = applySort(baseList)

                adapter.submitList(sortedList)
                updateEmptyState(sortedList)

                dialog.dismiss()
            }
        }
    }
}