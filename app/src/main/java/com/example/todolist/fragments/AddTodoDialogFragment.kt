package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.models.ToDoStatus
import com.example.todolist.models.TodoDetails
import com.example.todolist.viewmodel.TodoViewModel

class AddTodoDialogFragment : DialogFragment() {

    private lateinit var viewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.add_todo, container, false)
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

        val doneBtn = view.findViewById<View>(R.id.AddNewWordBtnDone)

        // ⚠️ Adjust these IDs to match your XML if needed
        val titleInput = view.findViewById<EditText>(R.id.AddNewTitleInput)
        val detailsInput = view.findViewById<EditText>(R.id.AddNewDetailsInput)
        val meaningInput = view.findViewById<EditText>(R.id.AddNewMeaningInput)
        val synonymInput = view.findViewById<EditText>(R.id.AddNewSynonymInput)

        doneBtn.setOnClickListener {

            val title = titleInput.text.toString()
            val details = detailsInput.text.toString()
            val meaning = meaningInput.text.toString()
            val synonym = synonymInput.text.toString()

            if (title.isNotEmpty() && details.isNotEmpty()) {

                val newTodo = TodoDetails(
                    title = title,
                    meaning = meaning,
                    synonyms = synonym,
                    details = details,
                    status = ToDoStatus.NEW,
                    date = System.currentTimeMillis()
                )

                viewModel.insert(newTodo)
            }

            dismiss()
        }
    }
}