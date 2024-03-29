package com.zeng.myworkout.view

import android.annotation.SuppressLint
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
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.FragmentHomeBinding
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupBinding()
        subscribeUi()
        setupButtons()
        return binding.root
    }

    private fun setupBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
    }

    private fun subscribeUi() {
        // Show current routine workout
        viewModel.user.observe(viewLifecycleOwner, Observer { it?.let { user ->
            binding.apply {
                if (user.workoutReferenceId != null) {
                    currentProgramLayout.visibility = View.VISIBLE
                    continueRoutine.visibility = View.VISIBLE
                } else {
                    currentProgramLayout.visibility = View.GONE
                    continueRoutine.visibility = View.GONE
                }
            }

            if (user.workoutSessionId != null && !user.customSession) {
                navigateToWorkout()
            }
        }})
    }

    private fun setupButtons() {
        binding.apply {
            continueRoutine.setOnClickListener {
                viewModel.viewModelScope.launch {
                    viewModel.continueRoutineWorkout()
                    navigateToWorkout()
                }
            }

            chooseRoutine.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_home_to_routine_nav)
            }

            startEmptyWorkout.setOnClickListener {
                createEmptyWorkout()
            }
        }
    }

    private fun navigateToWorkout() {
        val homeNav = findNavController().graph[R.id.home_nav] as NavGraph
        homeNav.startDestination = R.id.navigation_workout
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationWorkout()
        findNavController().navigate(action)
    }

    @SuppressLint("SimpleDateFormat")
    private fun createEmptyWorkout() {
        viewModel.viewModelScope.launch {
            val date = Date()
            val dateString = SimpleDateFormat("dd/MM/yyyy").format(date)
            val workout = Workout(name = "Workout - $dateString", startDate = date)
            viewModel.createWorkoutSession(workout)
            val user = viewModel.user.value!!
            user.workoutSessionId = workout.id
            user.customSession = true
            user.workoutReferenceId = null
            viewModel.updateUser(user)
            navigateToWorkout()
        }
    }
}