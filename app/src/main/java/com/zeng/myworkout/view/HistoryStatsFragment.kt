package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zeng.myworkout.databinding.FragmentHistoryStatsBinding

class HistoryStatsFragment : Fragment() {

    private lateinit var binding: FragmentHistoryStatsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryStatsBinding.inflate(inflater, container, false)

        return binding.root
    }

}
