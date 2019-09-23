package com.zeng.myworkout2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.zeng.myworkout2.databinding.FragmentWorkoutBinding
import com.zeng.myworkout2.model.Exercise
import com.zeng.myworkout2.model.WorkoutExerciseSql
import com.zeng.myworkout2.util.RepositoryUtils
import com.zeng.myworkout2.view.adapter.WorkoutExerciseAdapter
import com.zeng.myworkout2.viewmodel.WorkoutViewModel
import com.zeng.myworkout2.viewmodel.getViewModel

class WorkoutFragment(val workoutId: Long) : Fragment() {

    private lateinit var binding: FragmentWorkoutBinding

    val viewModel: WorkoutViewModel by lazy {
        val workoutRepository = RepositoryUtils.getWorkoutRepository(requireContext())
        getViewModel({ WorkoutViewModel(workoutRepository, workoutId)}, workoutId.toString())
    }

    private val adapter: WorkoutExerciseAdapter by lazy {
        WorkoutExerciseAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        binding.list.adapter = adapter

        val helper = ItemTouchHelper(adapter.callback)
        helper.attachToRecyclerView(binding.list)

        subscribeUi()

        return binding.root
    }

    fun subscribeUi() {
        viewModel.workout.observe(viewLifecycleOwner, Observer { workout ->
            adapter.submitList(workout.exercises)
        })
    }

    fun addExercise() {
        val ex = Exercise("Pullups", "Back")
        val exWorkout = WorkoutExerciseSql(5, 5, 20f, 1, workoutId)
        viewModel.insertExerciceTest(ex, exWorkout)
    }
}
