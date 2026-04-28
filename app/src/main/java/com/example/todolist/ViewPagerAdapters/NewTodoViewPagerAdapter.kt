package com.example.todolist.NewTodoViewPagerAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todolist.fragments.CompletedWordFragment
import com.example.todolist.fragments.NewTodoFragment
import com.example.todolist.fragments.TodoDetailsFragment

class NewTodoViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewTodoFragment()
            1 -> CompletedWordFragment()
            else -> NewTodoFragment()
        }
    }
}