package com.example.todolist.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todolist.R
import com.example.todolist.viewmodel.TodoViewModel

class TodoDetailsFragment : DialogFragment(R.layout.todo_details) {

    private lateinit var viewModel: TodoViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val doneBtn = view.findViewById<View>(R.id.detailsBtnDone)
        val deleteBtn = view.findViewById<View>(R.id.detailsBtnDelete)
        val updateBtn = view.findViewById<View>(R.id.detailsBtnUpdate)

        val title = view.findViewById<TextView>(R.id.titleTextView)
        val details = view.findViewById<TextView>(R.id.detailsTextView)
        val synonyms = view.findViewById<TextView>(R.id.synonymTextView)
        val meanings = view.findViewById<TextView>(R.id.meaningTextView)

        viewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        viewModel.selectedTodo.observe(viewLifecycleOwner) { todo ->

            if (todo == null) return@observe

            title.text = todo.title
            details.text = todo.details
            synonyms.text = todo.synonyms
            meanings.text = todo.meaning

            val doneBtn = view.findViewById<View>(R.id.detailsBtnDone)
            val updateBtn = view.findViewById<View>(R.id.detailsBtnUpdate)

            // 🔥 CHECK STATUS
            if (todo.status.name == "DONE") {

                doneBtn.visibility = View.GONE
                updateBtn.visibility = View.GONE

            } else {

                doneBtn.visibility = View.VISIBLE
                updateBtn.visibility = View.VISIBLE
            }
        }

        doneBtn.setOnClickListener {
            DoneDialogFragment.newInstance(viewModel.selectedTodo.value!!)
                .show(parentFragmentManager, "done")
        }

        deleteBtn.setOnClickListener {
            DeleteDialogFragment.newInstance(viewModel.selectedTodo.value!!)
                .show(parentFragmentManager, "delete")
        }

        updateBtn.setOnClickListener {
            UpdateTodoDialogFragment.newInstance(viewModel.selectedTodo.value!!)
                .show(parentFragmentManager, "update")
        }

    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        dialog?.window?.setBackgroundDrawableResource(android.R.color.white)
    }

}