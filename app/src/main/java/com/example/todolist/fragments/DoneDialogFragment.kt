package com.example.todolist.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.models.ToDoStatus
import com.example.todolist.models.TodoDetails
import com.example.todolist.viewmodel.TodoViewModel

class DoneDialogFragment : DialogFragment(R.layout.dialog_alert2) {

    private lateinit var viewModel: TodoViewModel
    private var todo: TodoDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todo = arguments?.getParcelable("todo")
    }

    companion object {
        fun newInstance(todo: TodoDetails): DoneDialogFragment {
            val fragment = DoneDialogFragment()

            val bundle = Bundle()
            bundle.putParcelable("todo", todo)

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        val cancelBtn = view.findViewById<View>(R.id.dialogBtnNo)
        val doneBtn = view.findViewById<View>(R.id.dialogBtnYes)

        cancelBtn.setOnClickListener {
            dismiss()
        }

        doneBtn.setOnClickListener {

            todo?.let { oldTodo ->

                val updatedTodo = oldTodo.copy(
                    status = ToDoStatus.DONE // 🔥 THIS MOVES IT TO COMPLETED PAGE
                )

                viewModel.update(updatedTodo)
            }

            dismiss()

            parentFragmentManager.findFragmentByTag("todo_details")?.let {
                (it as DialogFragment).dismiss()
            }
        }
    }
}