package com.example.todolist.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.RecycleViewAdapters.TodoRecycleViewAdapter
import com.example.todolist.models.TodoDetails
import com.example.todolist.viewmodel.TodoViewModel

class NewTodoFragment : Fragment(R.layout.new_todo_fragment) {

    private lateinit var viewModel: TodoViewModel

    // ✅ SORT STATE (ADDED)
    private var sortBy = "TITLE"   // TITLE or DATE
    private var sortOrder = "ASC"  // ASC or DESC

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycleView)
        val emptyView = view.findViewById<View>(R.id.emptyView)
        val sortButton = view.findViewById<ImageButton>(R.id.imageView)
        val addButton = view.findViewById<View>(R.id.button)
        val searchBar = view.findViewById<EditText>(R.id.editTextText)

        viewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        recyclerView.layoutManager = LinearLayoutManager(view.context)

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

        // ✅ APPLY SORT FUNCTION (ADDED)
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

        // 🔥 ROOM DATA OBSERVER
        viewModel.allTodos.observe(viewLifecycleOwner) { data ->

            baseList = data.filter { it.status.name == "NEW" }

            val sortedList = applySort(baseList)

            adapter.submitList(sortedList)
            updateEmptyState(sortedList)
        }

        // 🔍 SEARCH
        searchBar.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val query = s.toString().trim()

                val filteredList = if (query.isEmpty()) {
                    baseList
                } else {
                    baseList.filter {
                        it.title.contains(query, ignoreCase = true) ||
                                it.details.contains(query, ignoreCase = true)
                    }
                }

                val finalList = applySort(filteredList)

                adapter.submitList(finalList)
                updateEmptyState(finalList)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

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

            // ✅ RESTORE PREVIOUS SELECTION (ADDED)
            radioTitle.isChecked = sortBy == "TITLE"
            radioDate.isChecked = sortBy == "DATE"
            radioAsc.isChecked = sortOrder == "ASC"
            radioDesc.isChecked = sortOrder == "DESC"

            btnDone.setOnClickListener {

                // ✅ SAVE SELECTION (ADDED)
                sortBy = if (radioTitle.isChecked) "TITLE" else "DATE"
                sortOrder = if (radioDesc.isChecked) "DESC" else "ASC"

                val sortedList = applySort(baseList)

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