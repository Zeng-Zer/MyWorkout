package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.zeng.myworkout.databinding.FragmentHistoryStatsBinding
import com.zeng.myworkout.view.adapter.HistoryStatAdapter
import com.zeng.myworkout.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HistoryStatsFragment : Fragment() {

    private lateinit var binding: FragmentHistoryStatsBinding
    private val viewModel by sharedViewModel<HistoryViewModel>()
    private val adapter by lazy { HistoryStatAdapter(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryStatsBinding.inflate(inflater, container, false)

        setupRecyclerView()
        subscribeUi()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.list.adapter = adapter
    }

    private fun subscribeUi() {
        viewModel.workouts.observe(viewLifecycleOwner, Observer { workouts ->
            adapter.submitList(workouts)
        })
    }

}
