package com.zeng.myworkout.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.DialogFilterHistoryFormBinding
import com.zeng.myworkout.databinding.FragmentHistoryBinding
import com.zeng.myworkout.view.adapter.HistoryAdapter
import com.zeng.myworkout.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val adapter by lazy { HistoryAdapter(childFragmentManager) }
    private val viewModel by sharedViewModel<HistoryViewModel>()
    private val filterAdapters by lazy { initAdapters() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        setupViewPager()
        subscribeUi()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                openDialog()
            }
        }
        return true
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 2
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeUi() {
        viewModel.filters.observe(viewLifecycleOwner, Observer { (routineFilter, workoutFilter, exerciseFilter) ->
            if (routineFilter != "All" || workoutFilter != "All" || exerciseFilter != "All") {
                binding.filters.visibility = View.VISIBLE
                binding.filters.text = "Routine: $routineFilter - Workout: $workoutFilter - Exercise: $exerciseFilter"
            } else {
                binding.filters.visibility = View.GONE
            }
        })
    }

    private fun initAdapters(): Triple<ArrayAdapter<String>, ArrayAdapter<String>, ArrayAdapter<String>> {
        val adapters = Triple(
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf<String>()),
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf<String>()),
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf<String>())
        )
        for (adapter in adapters.toList()) {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        return adapters
    }

    private fun openDialog() {
        val savedFilters = viewModel.filters.value!!

        val view = DialogFilterHistoryFormBinding.inflate(layoutInflater, null, false)
        setupAdapters(view)
        setupSpinners(view)

        val dialog = AlertDialog.Builder(requireContext())
            .setMessage("Filter data")
            .setPositiveButton("Ok") { _, _ ->
                println("test")
            }
            .setNegativeButton("Cancel") {  _, _ ->
                viewModel.filters.value = savedFilters
            }
            .setNeutralButton("Reset") { _, _ ->
                viewModel.filters.value = Triple("All", "All", "All")
            }
            .setView(view.root)
            .create()

        dialog.show()
    }

    private fun setupAdapters(view: DialogFilterHistoryFormBinding) {
        viewModel.filters.observe(viewLifecycleOwner, Observer { (routineFilter, workoutFilter, exerciseFilter) ->
            val (routineAdapter, workoutAdapter, exerciseAdapter) = filterAdapters
            val workouts = viewModel.workouts.value!!
            val routineNames = workouts
                .map { it.routine ?: "No Routine" }
                .distinct()
                .toMutableList()
            routineNames.add(0, "All")
            routineAdapter.clear()
            routineAdapter.addAll(routineNames)

            // Apply routine Filter
            val workoutsFilteredByRoutine = workouts
                .filter { routineFilter == "All" || (it.routine ?: "No Routine") == routineFilter }

            val workoutNames = workoutsFilteredByRoutine
                .map { it.workout.name }
                .distinct()
                .toMutableList()
            workoutNames.add(0, "All")
            workoutAdapter.clear()
            workoutAdapter.addAll(workoutNames)
            if (!workoutNames.contains(workoutFilter)) {
                view.spinnerWorkout.setSelection(0)
            }

            val exerciseNames = workoutsFilteredByRoutine
                // Apply workout filter
                .filter {
                    workoutFilter == "All" || it.workout.name == workoutFilter
                }
                .flatMap { workout ->
                    workout.exercises.map { it.detail.name }
                }
                .distinct()
                .toMutableList()
            exerciseNames.add(0, "All")
            exerciseAdapter.clear()
            exerciseAdapter.addAll(exerciseNames)
            if (!exerciseNames.contains(exerciseFilter)) {
                view.spinnerExercise.setSelection(0)
            }
        })
    }

    private fun setupSpinners(view: DialogFilterHistoryFormBinding) {
        val (routineFilter, workoutFilter, exerciseFilter) = viewModel.filters.value!!
        val (routineAdapter, workoutAdapter, exerciseAdapter) = filterAdapters
        view.spinnerRoutine.adapter = routineAdapter
        view.spinnerRoutine.setSelection(routineAdapter.getPosition(routineFilter))
        view.spinnerRoutine.onItemSelectedListener = SpinnerOnItemSelectedListener(FilterType.ROUTINE, true)

        view.spinnerWorkout.adapter = workoutAdapter
        view.spinnerWorkout.setSelection(workoutAdapter.getPosition(workoutFilter))
        view.spinnerWorkout.onItemSelectedListener = SpinnerOnItemSelectedListener(FilterType.WORKOUT, true)

        view.spinnerExercise.adapter = exerciseAdapter
        view.spinnerExercise.setSelection(exerciseAdapter.getPosition(exerciseFilter))
        view.spinnerExercise.onItemSelectedListener = SpinnerOnItemSelectedListener(FilterType.EXERCISE, true)
    }

    private inner class SpinnerOnItemSelectedListener(private val type: FilterType, private var init: Boolean) : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            if (!init) {
                val (routineAdapter, workoutAdapter, exerciseAdapter) = filterAdapters
                var (rf, wf, ef) = viewModel.filters.value!!
                when (type) {
                    FilterType.ROUTINE -> rf = routineAdapter.getItem(position)!!
                    FilterType.WORKOUT -> wf = workoutAdapter.getItem(position)!!
                    FilterType.EXERCISE -> ef = exerciseAdapter.getItem(position)!!
                }
                viewModel.filters.value = Triple(rf, wf, ef)
            }
            init = false
        }
    }

    private enum class FilterType {
        ROUTINE,
        WORKOUT,
        EXERCISE
    }

}