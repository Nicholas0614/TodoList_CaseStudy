package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.models.TodoDetails
import com.example.todolist.viewmodel.TodoViewModel

class DeleteDialogFragment : DialogFragment() {

    private lateinit var viewModel: TodoViewModel
    private var todo: TodoDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todo = arguments?.getParcelable("todo")
    }

    companion object {
        fun newInstance(todo: TodoDetails): DeleteDialogFragment {
            val fragment = DeleteDialogFragment()

            val bundle = Bundle()
            bundle.putParcelable("todo", todo)

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        val cancelBtn = view.findViewById<View>(R.id.dialogBtnCancel)
        val deleteBtn = view.findViewById<View>(R.id.dialogBtnDelete)

        cancelBtn.setOnClickListener {
            dismiss()
        }

        deleteBtn.setOnClickListener {

            todo?.let {
                viewModel.delete(it)   // 🔥 ACTUAL DELETE
            }

            dismiss()


            parentFragmentManager.findFragmentByTag("todo_details")?.let {
                (it as DialogFragment).dismiss() // 🔥 close details dialog too
            }
        }
    }
}