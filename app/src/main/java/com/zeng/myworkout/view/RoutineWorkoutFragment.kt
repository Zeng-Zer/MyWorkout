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
import com.zeng.myworkout.databinding.FragmentRoutineWorkoutBinding
import com.zeng.myworkout.view.adapter.WorkoutExerciseAdapter
import com.zeng.myworkout.viewmodel.RoutineWorkoutViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RoutineWorkoutFragment(private val workoutId: Long) : Fragment() {

    private lateinit var binding: FragmentRoutineWorkoutBinding
    private val viewModel by viewModel<RoutineWorkoutViewModel> { parametersOf(workoutId) }
    private lateinit var adapter: WorkoutExerciseAdapter
    private val exerciseRecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRoutineWorkoutBinding.inflate(inflater, container, false)

        setupAdapter()
        setupRecyclerView()
        subscribeUi()
        return binding.root
    }

    private fun setupAdapter() {
        adapter = WorkoutExerciseAdapter(requireContext(), exerciseRecycledViewPool)
        adapter.enableDrag()
    }

    private fun setupRecyclerView() {
//        binding.list.setRecycledViewPool(workoutRecycledViewPool)
        binding.list.adapter = adapter

        // prevent RecyclerView blinking on submitList
        val animator = binding.list.itemAnimator as SimpleItemAnimator
        animator.supportsChangeAnimations = false

        val helper = ItemTouchHelper(adapter.callback)
        helper.attachToRecyclerView(binding.list)
    }

    private fun subscribeUi() {
        viewModel.exercises.observe(viewLifecycleOwner, Observer { exercises ->
            adapter.submitList(exercises)
        })
    }
}