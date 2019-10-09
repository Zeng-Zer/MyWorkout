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
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.FragmentWorkoutBinding
import com.zeng.myworkout.viewmodel.HomeViewModel
import com.zeng.myworkout.viewmodel.WorkoutViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WorkoutFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutBinding
    private val homeVm by sharedViewModel<HomeViewModel>()
    private val workoutVm by viewModel<WorkoutViewModel>()
    private val recycledViewPool by lazy { RecyclerView.RecycledViewPool() }
//    private lateinit var adapter: WorkoutExerciseAdapter
//    private val adapter: WorkoutExerciseAdapter by lazy { WorkoutExerciseAdapter(recycledViewPool, workoutVm, true) }

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
                homeVm.viewModelScope.launch {
                    homeVm.deleteWorkoutSession()
                    navigateToHome()
                }
            }
            finish.setOnClickListener {
                homeVm.viewModelScope.launch {
                    homeVm.finishCurrentWorkoutSession()
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
//        binding.apply {
//            list.adapter = adapter
//
//            // prevent RecyclerView blinking on submitList
//            val animator = list.itemAnimator as SimpleItemAnimator
//            animator.supportsChangeAnimations = false
//
//            val helper = ItemTouchHelper(adapter.callback)
//            helper.attachToRecyclerView(list)
//        }
    }

    private fun subscribeUi() {
        homeVm.workoutSession.observe(viewLifecycleOwner, Observer { it?.let { workout ->
//            if (!::adapter.isInitialized) {
//                adapter = WorkoutExerciseAdapter(recycledViewPool, workoutVm, true)
//            }
//            workout.id?.let { id -> workoutVm.setLiveDataWorkoutId(id) }
            (requireActivity() as MainActivity).supportActionBar?.title = workout.name
        }})

//        workoutVm.exercises.observe(viewLifecycleOwner, Observer { exercises ->
//            adapter.submitList(exercises)
//        })
    }

}