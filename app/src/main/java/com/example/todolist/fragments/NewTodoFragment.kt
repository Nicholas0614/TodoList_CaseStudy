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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycleView)
        val emptyView = view.findViewById<View>(R.id.emptyView)
        val sortButton = view.findViewById<ImageButton>(R.id.imageView)
        val addButton = view.findViewById<View>(R.id.button)
        val searchBar = view.findViewById<EditText>(R.id.editTextText)

        viewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        recyclerView.layoutManager = LinearLayoutManager(view.context)

        // ✅ Adapter (kept same logic)
        val adapter = TodoRecycleViewAdapter { todo ->

            viewModel.selectedTodo.value = todo

            TodoDetailsFragment().show(
                parentFragmentManager,
                "todo_details"
            )
        }

        recyclerView.adapter = adapter

        // ✅ base list (updated from Room)
        var baseList = listOf<TodoDetails>()

        // ✅ Empty state
        fun updateEmptyState(list: List<TodoDetails>) {
            if (list.isEmpty()) {
                emptyView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        // 🔥 ROOM DATA OBSERVER (REPLACES MOCK DATA)
        viewModel.allTodos.observe(viewLifecycleOwner) { data ->

            baseList = data.filter { it.status.name == "NEW" }

            adapter.submitList(baseList)
            updateEmptyState(baseList)
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

                adapter.submitList(filteredList)
                updateEmptyState(filteredList)
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
                dialog.dismiss()
            }
        }

        // ➕ ADD TODO (still not wired to Room yet)
        addButton.setOnClickListener {
            AddTodoDialogFragment().show(parentFragmentManager, "add_todo")
        }
    }
}