package com.zeng.myworkout2.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.zeng.myworkout2.view.WorkoutFragment

class RoutineDetailAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var workouts: List<WorkoutFragment> = emptyList()
    override fun getItem(position: Int): Fragment {
        return workouts[position]
    }

    override fun getCount(): Int {
        return workouts.size
    }

    fun submitList(workouts: List<WorkoutFragment>) {
        this.workouts = workouts
        notifyDataSetChanged()
    }
//
//    override fun getItem(position: Int): WorkoutFragment {
//        return fragments[position]
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return fragments[position].workout.name
//    }
//
//    override fun getCount(): Int {
//        // Show 2 total pages.
//        return fragments.size
//    }
//
//    override fun getItemPosition(`object`: Any): Int {
//        return PagerAdapter.POSITION_NONE
//    }
//
//    fun removeTab(position: Int) {
//        if (fragments.size > position) {
//            fragments.removeAt(position)
//            notifyDataSetChanged()
//        }
//    }
//
//    fun addTab(fragment: WorkoutFragment, position: Int = fragments.size) {
//        fragments.add(position, fragment)
//        notifyDataSetChanged()
//    }
//
//    fun swapTab(position1: Int, position2: Int) {
//        if (position1 < fragments.size && position2 < fragments.size && position1 >= 0 && position2 >= 0) {
//            fragments[position1] = fragments[position2].also { fragments[position2] = fragments[position1] }
//            notifyDataSetChanged()
//        }
//    }
//
}