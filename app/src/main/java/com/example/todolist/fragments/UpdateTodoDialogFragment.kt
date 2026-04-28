package com.example.todolist.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.models.TodoDetails
import com.example.todolist.viewmodel.TodoViewModel

class UpdateTodoDialogFragment : DialogFragment(R.layout.update_todo) {

    private lateinit var viewModel: TodoViewModel
    private var todo: TodoDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todo = arguments?.getParcelable("todo")
    }

    companion object {
        fun newInstance(todo: TodoDetails): UpdateTodoDialogFragment {
            val fragment = UpdateTodoDialogFragment()

            val bundle = Bundle()
            bundle.putParcelable("todo", todo)

            fragment.arguments = bundle
            return fragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        // 🔥 UI elements
        val titleInput = view.findViewById<EditText>(R.id.UpdateNewTitleInput)
        val detailsInput = view.findViewById<EditText>(R.id.UpdateNewDetailsInput)
        val synonymsInput = view.findViewById<EditText>(R.id.UpdateNewSynonymInput)
        val meaningInput = view.findViewById<EditText>(R.id.UpdateNewMeaningInput)

        val updateBtn = view.findViewById<View>(R.id.AddNewWordBtnDone)

        // 🔥 Pre-fill data
        todo?.let {
            titleInput.setText(it.title)
            detailsInput.setText(it.details)
            synonymsInput.setText(it.synonyms)
            meaningInput.setText(it.meaning)
        }

        // ✅ Update
        updateBtn.setOnClickListener {

            todo?.let { oldTodo ->

                val updatedTodo = oldTodo.copy(
                    title = titleInput.text.toString(),
                    details = detailsInput.text.toString(),
                    synonyms = synonymsInput.text.toString(),
                    meaning = meaningInput.text.toString()
                )

                viewModel.update(updatedTodo)

                viewModel.refreshSelectedTodo(updatedTodo.id)
            }

            dismiss()
        }
    }
}