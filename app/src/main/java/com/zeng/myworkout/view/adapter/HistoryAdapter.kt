package com.zeng.myworkout.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zeng.myworkout.view.HistoryHistoryFragment
import com.zeng.myworkout.view.HistoryStatsFragment

class HistoryAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HistoryHistoryFragment()
            else -> HistoryStatsFragment()
        }
    }

}