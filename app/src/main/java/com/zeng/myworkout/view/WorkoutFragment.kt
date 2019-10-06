package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.FragmentWorkoutBinding
import com.zeng.myworkout.util.RepositoryUtils
import com.zeng.myworkout.view.adapter.WorkoutExerciseAdapter
import com.zeng.myworkout.viewmodel.HomeViewModel
import com.zeng.myworkout.viewmodel.WorkoutViewModel
import com.zeng.myworkout.viewmodel.getSharedViewModel
import com.zeng.myworkout.viewmodel.getViewModel
import kotlinx.coroutines.launch

class WorkoutFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutBinding

    private val homeViewModel by lazy {
        getSharedViewModel({
            HomeViewModel(
                RepositoryUtils.getRoutineRepository(requireContext()),
                RepositoryUtils.getWorkoutRepository(requireContext())
            )
        })
    }

    private val workoutViewModel by lazy {
        getViewModel({
            WorkoutViewModel(RepositoryUtils.getWorkoutRepository(requireContext()))
        })
    }

    private val adapter: WorkoutExerciseAdapter by lazy { WorkoutExerciseAdapter(recycledViewPool, workoutViewModel, true) }

    private val recycledViewPool by lazy { RecyclerView.RecycledViewPool() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false)

        setupButtons()
        setupRecyclerView()
        subscribeUi()
        return binding.root
    }

    private fun setupButtons() {
        binding.apply {
            cancel.setOnClickListener {
                homeViewModel.viewModelScope.launch {
                    homeViewModel.deleteWorkoutSession()
                }
            }
            finish.setOnClickListener {
                homeViewModel.viewModelScope.launch {
                    homeViewModel.finishCurrentWorkoutSession()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            list.adapter = adapter

            // prevent RecyclerView blinking on submitList
            val animator = list.itemAnimator as SimpleItemAnimator
            animator.supportsChangeAnimations = false

            val helper = ItemTouchHelper(adapter.callback)
            helper.attachToRecyclerView(list)
        }
    }

    private fun subscribeUi() {
        homeViewModel.workoutSession.observe(viewLifecycleOwner, Observer { workout ->
            if (workout != null) {
                workout.id?.let { id -> workoutViewModel.setLiveDataWorkoutId(id) }
                (requireActivity() as MainActivity).supportActionBar?.title = workout.name
            } else {
                findNavController().navigate(R.id.action_navigation_workout_to_navigation_home)
            }
        })

        workoutViewModel.exercises.observe(viewLifecycleOwner, Observer { exercises ->
            adapter.submitList(exercises)
        })
    }

}