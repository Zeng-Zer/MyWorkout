package com.zeng.myworkout.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zeng.myworkout.view.HistoryHistoryFragment
import com.zeng.myworkout.view.HistoryStatsFragment

class HistoryAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0    -> HistoryHistoryFragment()
            else -> HistoryStatsFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0    -> "History"
            else -> "Stats"
        }
    }

}