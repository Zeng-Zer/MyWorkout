package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.zeng.myworkout.databinding.FragmentHistoryHistoryBinding
import com.zeng.myworkout.model.WorkoutWithExercises
import com.zeng.myworkout.view.adapter.HistoryHistoryAdapter
import com.zeng.myworkout.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HistoryHistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryHistoryBinding
    private val viewModel by sharedViewModel<HistoryViewModel>()
    private val adapter by lazy { HistoryHistoryAdapter(requireContext()) }

    private lateinit var workouts: List<WorkoutWithExercises>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryHistoryBinding.inflate(inflater, container, false)

        setupRecyclerView()
        subscribeUi()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.list.adapter = adapter
    }

    private fun submitFilteredList(routineFilter: String, workoutFilter: String, exerciseFilter: String) {
        val filteredList = workouts
            .filter { routineFilter == "All" || it.routine ?: "No Routine" == routineFilter  }
            .filter { workoutFilter == "All" || it.workout.name == workoutFilter }
            .mapNotNull {
                val exercises = it.exercises
                    .filter { ex -> exerciseFilter == "All" || ex.detail.name == exerciseFilter }
                if (exercises.isEmpty()) {
                    null
                } else {
                    WorkoutWithExercises(it.workout, exercises, it.routine)
                }
            }
        adapter.submitList(filteredList)
    }

    private fun subscribeUi() {
        viewModel.workouts.observe(viewLifecycleOwner, Observer { workouts ->
            this.workouts = workouts
            val (rf, wf, ef) = viewModel.filters.value!!
            submitFilteredList(rf, wf, ef)
        })

        viewModel.filters.observe(viewLifecycleOwner, Observer { (rf, wf, ef) ->
            if (::workouts.isInitialized) {
                submitFilteredList(rf, wf, ef)
            }
        })
    }

}