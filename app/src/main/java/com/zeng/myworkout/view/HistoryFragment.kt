package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zeng.myworkout.databinding.FragmentHistoryBinding
import com.zeng.myworkout.view.adapter.HistoryAdapter

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val adapter by lazy { HistoryAdapter(childFragmentManager) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        setupViewPager()
        return binding.root
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 2
        binding.tabs.setupWithViewPager(binding.viewPager)
    }
}