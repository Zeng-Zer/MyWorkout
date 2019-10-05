package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zeng.myworkout.databinding.FragmentHomeWorkoutBinding
import com.zeng.myworkout.util.RepositoryUtils
import com.zeng.myworkout.viewmodel.HomeViewModel
import com.zeng.myworkout.viewmodel.getViewModel

class HomeWorkoutFragment : Fragment() {

    private lateinit var binding: FragmentHomeWorkoutBinding

    private val viewModel by lazy {
        getViewModel({
            HomeViewModel(
                RepositoryUtils.getRoutineRepository(requireContext()),
                RepositoryUtils.getWorkoutRepository(requireContext())
            )
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeWorkoutBinding.inflate(inflater, container, false)

        binding.cancel.setOnClickListener {
            viewModel.updateUserSessionWorkout(null)
        }

        return binding.root
    }
}