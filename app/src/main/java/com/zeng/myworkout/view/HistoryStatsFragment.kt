package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.zeng.myworkout.databinding.FragmentHistoryStatsBinding
import com.zeng.myworkout.model.WorkoutWithExercises
import com.zeng.myworkout.view.adapter.HistoryStatAdapter
import com.zeng.myworkout.viewmodel.GroupedWorkouts
import com.zeng.myworkout.viewmodel.HistoryViewModel
import com.zeng.myworkout.viewmodel.WorkoutExercisesByName
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HistoryStatsFragment : Fragment() {

    private lateinit var binding: FragmentHistoryStatsBinding
    private val viewModel by sharedViewModel<HistoryViewModel>()
    private val navController by lazy { findNavController() }
    private val adapter by lazy {
        HistoryStatAdapter(
            requireContext(),
            onItemClick = this::navChartExerciseFragment
        )
    }

    private lateinit var groupedWorkouts: List<GroupedWorkouts>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryStatsBinding.inflate(inflater, container, false)

        setupRecyclerView()
        subscribeUi()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.list.adapter = adapter
    }

    private fun submitFilteredList(routineFilter: String, workoutFilter: String, exerciseFilter: String) {
        val filteredList: List<GroupedWorkouts> = groupedWorkouts
            .filter { routineFilter == "All" || it.first.first!! == routineFilter }
            .filter { workoutFilter == "All" || it.first.second == workoutFilter }
            .mapNotNull { gw ->
                val workoutWithExercises = gw.second.mapNotNull { workout ->
                    val exercises = workout.exercises.filter { exerciseFilter == "All" || it.detail.name == exerciseFilter }
                    if (exercises.isEmpty()) {
                        null
                    } else {
                        WorkoutWithExercises(workout.workout, exercises, workout.routine)
                    }
                }
                if (workoutWithExercises.isEmpty()) {
                    null
                } else {
                    Pair(gw.first, workoutWithExercises)
                }
            }
        adapter.submitList(filteredList)
    }

    private fun subscribeUi() {
        viewModel.groupedWorkouts.observe(viewLifecycleOwner, Observer { workouts ->
            groupedWorkouts = workouts
                // filter workouts without routine name
                .filter { !it.first.first.isNullOrBlank() }
                .sortedBy { it.second.first().workout.startDate }

            val (rf, wf, ef) = viewModel.filters.value!!
            submitFilteredList(rf, wf, ef)
        })

        viewModel.filters.observe(viewLifecycleOwner, Observer { (rf, wf, ef) ->
            if (::groupedWorkouts.isInitialized) {
                submitFilteredList(rf, wf, ef)
            }
        })
    }

    private fun navChartExerciseFragment(routineName: String, workoutName: String, exercises: List<WorkoutExercisesByName>) {
        viewModel.workoutExercisesByNames.value = exercises
        val action = HistoryStatsFragmentDirections.actionNavigationStatToNavigationChartExercise("$routineName $workoutName")
        navController.navigate(action)
    }

}
