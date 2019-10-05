package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zeng.myworkout.databinding.FragmentHomeWorkoutBinding
import com.zeng.myworkout.util.RepositoryUtils
import com.zeng.myworkout.view.adapter.WorkoutExerciseAdapter
import com.zeng.myworkout.viewmodel.HomeViewModel
import com.zeng.myworkout.viewmodel.WorkoutViewModel
import com.zeng.myworkout.viewmodel.getViewModel

class HomeWorkoutFragment : Fragment() {

    private lateinit var binding: FragmentHomeWorkoutBinding

    private val homeViewModel by lazy {
        getViewModel({
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
        binding = FragmentHomeWorkoutBinding.inflate(inflater, container, false)

        setupButtons()
        setupRecyclerView()
        subscribeUi()
        return binding.root
    }

    private fun setupButtons() {
        binding.apply {
            cancel.setOnClickListener {
                homeViewModel.deleteSessionWorkout()
                homeViewModel.updateUserSessionWorkout(null)
            }
            finish.setOnClickListener {
                homeViewModel.finishCurrentSessionWorkout()
                homeViewModel.updateToNextWorkout()
                homeViewModel.updateUserSessionWorkout(null)
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
        homeViewModel.sessionWorkout.observe(viewLifecycleOwner, Observer { it?.let { workout ->
            workout.id?.let { id ->
                workoutViewModel.setWorkoutId(id)
            }
            (requireActivity() as MainActivity).supportActionBar?.title = workout.name
        }})

        workoutViewModel.exercises.observe(viewLifecycleOwner, Observer { exercises ->
            adapter.submitList(exercises)
        })
    }

}