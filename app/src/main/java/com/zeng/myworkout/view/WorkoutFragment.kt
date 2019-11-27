package com.zeng.myworkout.view

import android.os.Bundle
import android.view.*
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
import com.zeng.myworkout.logic.setButtonEdit
import com.zeng.myworkout.logic.setButtonSessionReps
import com.zeng.myworkout.logic.setTextEditLoad
import com.zeng.myworkout.logic.showWorkoutExerciseMenuPopup
import com.zeng.myworkout.model.*
import com.zeng.myworkout.util.DialogUtils
import com.zeng.myworkout.view.adapter.WorkoutExerciseAdapter
import com.zeng.myworkout.viewmodel.ExerciseViewModel
import com.zeng.myworkout.viewmodel.HomeViewModel
import com.zeng.myworkout.viewmodel.WorkoutViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class WorkoutFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutBinding
    private val navController by lazy { findNavController() }
    private val sharedViewModel by sharedViewModel<ExerciseViewModel>()
    private val homeViewModel by viewModel<HomeViewModel>()
    private val workoutViewModel by viewModel<WorkoutViewModel> { parametersOf(this) }
    private val recycledViewPool by lazy { RecyclerView.RecycledViewPool() }
    private var user: User? = null
    private val adapter by lazy {
        val customSession = user?.customSession == true
        val fn = { if (customSession) {
            setButtonEdit(requireContext(), workoutViewModel, true)
        } else {
            setButtonSessionReps(requireContext(), workoutViewModel)
        }}()
        WorkoutExerciseAdapter(
            context = requireContext(),
            recycledViewPool = recycledViewPool,
            onClearView = { list -> workoutViewModel.updateWorkoutExercise(list.map{ it.exercise }) },
            onMenuClick = showWorkoutExerciseMenuPopup(requireContext(), workoutViewModel),
            onLoadClickNested = fn,
            onLoadTextClickNested = setTextEditLoad(requireContext(), workoutViewModel),
            session = true,
            customSession = customSession
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).supportActionBar?.title = ""

        user = homeViewModel.currentUser()
        if (user?.customSession == true) {
            setHasOptionsMenu(true)
        }
        setupButtons()
        setupRecyclerView()
        subscribeUi()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.workout_page_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
            }
            R.id.add_exercise -> {
                navController.navigate(R.id.action_navigation_workout_to_navigation_exercise)
            }

        }
        return true
    }

    private fun setupButtons() {
        binding.apply {
            cancel.setOnClickListener {
                homeViewModel.viewModelScope.launch {
                    val user = homeViewModel.user.value!!
                    val workoutId = user.workoutSessionId!!
                    user.customSession = false
                    user.workoutSessionId = null
                    homeViewModel.updateUser(user)
                    homeViewModel.deleteWorkout(workoutId)
                    navigateToHome()
                }
            }
            finish.setOnClickListener {
                homeViewModel.viewModelScope.launch {
                    val user = homeViewModel.user.value!!
                    homeViewModel.finishCurrentWorkoutSession(!user.customSession)
                    if (user.customSession) {
                        user.customSession = false
                        user.workoutSessionId = null
                        homeViewModel.updateUser(user)
                        saveSessionAsRoutine()
                    } else {
                        navigateToHome()
                    }
                }
            }
        }
    }

    private fun navigateToHome() {
        val homeNav = navController.graph[R.id.home_nav] as NavGraph
        homeNav.startDestination = R.id.navigation_home
        navController.navigate(R.id.action_navigation_workout_to_navigation_home)
    }

    private fun setupRecyclerView() {
        binding.apply {
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

        // Add new exercises from ExerciseFragment
        sharedViewModel.exercisesToAdd.observe(viewLifecycleOwner, Observer { exercises ->
            if (!exercises.isNullOrEmpty()) {
                addExercises(exercises.map { it.id!! })
                sharedViewModel.exercisesToAdd.value = null
            }
        })
    }

    private fun addExercises(exerciseIds: List<Long>) {
        val exercises = exerciseIds.mapIndexed { i, exerciseId ->
            val repsDone = if (user?.customSession == true) 1 else -1
            WorkoutExercise(
                // Add element at the end with its order in the list of ids
                i + adapter.itemCount,
                listOf(Load(LoadType.WEIGHT, 0F, 0, repsDone)),
                workoutViewModel.workoutId.value!!,
                exerciseId
            )
        }
        workoutViewModel.insertWorkoutExercises(exercises)
    }

    private fun saveSessionAsRoutine() {
        DialogUtils.openValidationDialog(
            context = requireContext(),
            message = "Save as a new routine ?",
            negative = "No",
            positiveFun = {
                workoutViewModel.viewModelScope.launch {
                    val exercises = workoutViewModel.exercises.value!!.map { it.copy().apply {
                        exercise.id = null
                        exercise.loads = exercise.loads.map { it.apply {
                            reps = repsDone
                            repsDone = -1
                        }}
                    }}
                    val description = exercises.fold("") { acc, exercise ->
                        acc + exercise.exercise.loads.size + "x" + (exercise.exercise.loads.first().reps) + " " + exercise.detail.name + "\n"
                    }
                    val routine = Routine(name = Date().toString(), description = description)
                    workoutViewModel.insertRoutine(routine)
                    val workout = Workout(name = "Workout", routineId = routine.id, reference = true)
                    workoutViewModel.insertWorkout(workout)
                    workoutViewModel.insertWorkoutExercises(exercises.map { it.exercise.apply { workoutId = workout.id } })
                    navigateToHome()
                }
            }
        )
    }
}