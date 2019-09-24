package com.zeng.myworkout2.view

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zeng.myworkout2.databinding.ListItemWorkoutBinding
import com.zeng.myworkout2.model.Exercise
import com.zeng.myworkout2.model.WorkoutExerciseSql
import com.zeng.myworkout2.view.adapter.WorkoutExerciseAdapter
import com.zeng.myworkout2.viewmodel.WorkoutViewModel

class WorkoutItem(private val lifecycleOwner: LifecycleOwner, val workoutId: Long, val viewModel: WorkoutViewModel) {

    lateinit var binding: ListItemWorkoutBinding

    private val adapter: WorkoutExerciseAdapter by lazy {
        WorkoutExerciseAdapter(viewModel)
    }

    fun init() {
        setupRecyclerView()
        subscribeUi()
    }

    private fun setupRecyclerView() {
        binding.list.adapter = adapter

        // prevent RecyclerView blinking on submitList
        val animator = binding.list.itemAnimator as SimpleItemAnimator
        animator.supportsChangeAnimations = false

        val helper = ItemTouchHelper(adapter.callback)
        helper.attachToRecyclerView(binding.list)
    }

    private fun subscribeUi() {
        viewModel.exercises.observe(lifecycleOwner, Observer { exercises ->
            adapter.submitList(exercises)
        })
    }

    fun addExercise() {
        val ex = Exercise("Pullups", "Back")
        val exWorkout = WorkoutExerciseSql(5, 5, 20f, 1, workoutId)
        viewModel.insertExerciceTest(ex, exWorkout)
    }
}
