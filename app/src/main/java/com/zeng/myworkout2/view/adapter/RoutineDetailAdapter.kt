package com.zeng.myworkout2.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zeng.myworkout2.view.WorkoutFragment

// TODO DIFFUTILS
class RoutineDetailAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    var workouts: List<WorkoutFragment> = emptyList()

    override fun getItemCount(): Int {
        return workouts.size
    }

    override fun createFragment(position: Int): Fragment {
        return workouts[position]
    }

    fun submitList(workouts: List<WorkoutFragment>) {
        this.workouts = workouts
        notifyDataSetChanged()
    }
}