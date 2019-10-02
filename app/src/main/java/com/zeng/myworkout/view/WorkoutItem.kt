package com.zeng.myworkout.view

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zeng.myworkout.databinding.ListItemWorkoutBinding
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.view.adapter.WorkoutExerciseAdapter
import com.zeng.myworkout.viewmodel.WorkoutViewModel

class WorkoutItem(private val lifecycleOwner: LifecycleOwner, val workoutId: Long, val viewModel: WorkoutViewModel) {

    lateinit var binding: ListItemWorkoutBinding

    private val adapter: WorkoutExerciseAdapter by lazy {
        WorkoutExerciseAdapter(viewModel, lifecycleOwner)
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

    // TODO THINK OF A WAY TO HANDLE DEFAULT VALUES
    fun addExercises(exerciseIds: Array<Long>) {
        val exercises = exerciseIds.mapIndexed { i, exerciseId ->
            WorkoutExercise(
                1,
                0,
                0f,
                // Add element at the end with its order in the list of ids
                i + adapter.itemCount,
                workoutId,
                exerciseId
            )
        }
        viewModel.insertWorkoutExercise(exercises)
    }
}
