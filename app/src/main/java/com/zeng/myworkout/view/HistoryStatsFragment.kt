package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.zeng.myworkout.databinding.FragmentHistoryStatsBinding
import com.zeng.myworkout.view.adapter.HistoryStatAdapter
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
        viewModel.groupedWorkouts.observe(viewLifecycleOwner, Observer { workouts ->
            val sortedWorkouts = workouts
                // filter workouts without routine name
                .filter { !it.first.first.isNullOrBlank() }
                .sortedBy { it.second.first().workout.startDate }

            adapter.submitList(sortedWorkouts)
        })
    }

    private fun navChartExerciseFragment(routineName: String, workoutName: String, exercises: List<WorkoutExercisesByName>) {
        viewModel.workoutExercisesByNames.value = exercises
        val action = HistoryStatsFragmentDirections.actionNavigationStatToNavigationChartExercise("$routineName $workoutName")
        navController.navigate(action)
    }

}
