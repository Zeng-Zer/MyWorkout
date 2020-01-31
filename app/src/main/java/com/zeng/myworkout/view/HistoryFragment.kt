package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.zeng.myworkout.databinding.FragmentHistoryBinding
import com.zeng.myworkout.view.adapter.HistoryAdapter
import com.zeng.myworkout.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val adapter by lazy { HistoryAdapter(requireActivity()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        setupViewPager()
        return binding.root
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 2
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            when (position) {
                0    -> tab.text = "History"
                else -> tab.text = "Stats"
            }
        }.attach()
    }
}