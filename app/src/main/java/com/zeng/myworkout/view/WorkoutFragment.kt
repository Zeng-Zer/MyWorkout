package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import androidx.navigation.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.FragmentWorkoutBinding
import com.zeng.myworkout.logic.setButtonSessionReps
import com.zeng.myworkout.logic.setTextEditLoad
import com.zeng.myworkout.logic.showWorkoutExerciseMenuPopup
import com.zeng.myworkout.view.adapter.WorkoutExerciseAdapter
import com.zeng.myworkout.viewmodel.HomeViewModel
import com.zeng.myworkout.viewmodel.WorkoutViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WorkoutFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutBinding
    private val homeViewModel by viewModel<HomeViewModel>()
    private val workoutViewModel by viewModel<WorkoutViewModel> { parametersOf(this) }
    private val recycledViewPool by lazy { RecyclerView.RecycledViewPool() }
    private val adapter by lazy { WorkoutExerciseAdapter(
        context = requireContext(),
        recycledViewPool = recycledViewPool,
        session = true,
        onClearView = { list -> workoutViewModel.updateAllWorkoutExercise(list.map{ it.exercise }) },
        onMenuClick = showWorkoutExerciseMenuPopup(requireContext(), workoutViewModel),
        onLoadClickNested = setButtonSessionReps(requireContext(), workoutViewModel),
        onLoadTextClickNested = setTextEditLoad(requireContext(), workoutViewModel)
    )}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).supportActionBar?.title = ""

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
                    navigateToHome()
                }
            }
            finish.setOnClickListener {
                homeViewModel.viewModelScope.launch {
                    homeViewModel.finishCurrentWorkoutSession()
                    navigateToHome()
                }
            }
        }
    }

    private fun navigateToHome() {
        val homeNav = findNavController().graph[R.id.home_nav] as NavGraph
        homeNav.startDestination = R.id.navigation_home
        findNavController().navigate(R.id.action_navigation_workout_to_navigation_home)
    }

    private fun setupRecyclerView() {
        binding.apply {
            adapter.viewLifecycleOwner = viewLifecycleOwner
            adapter.enableDrag()
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
            (requireActivity() as MainActivity).supportActionBar?.title = workout?.name
            workoutViewModel.workoutId.value = workout?.id
        })

        workoutViewModel.exercises.observe(viewLifecycleOwner, Observer { exercises ->
            adapter.submitList(exercises)
        })
    }
}