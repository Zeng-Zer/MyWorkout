package com.zeng.myworkout.view

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zeng.myworkout.databinding.FragmentRoutineWorkoutBinding
import com.zeng.myworkout.logic.setButtonEdit
import com.zeng.myworkout.logic.setTextEditLoad
import com.zeng.myworkout.logic.showWorkoutExerciseMenuPopup
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.LoadType
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.view.adapter.WorkoutExerciseAdapter
import com.zeng.myworkout.viewmodel.ExerciseViewModel
import com.zeng.myworkout.viewmodel.WorkoutViewModel
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class RoutineWorkoutViewHolder(
    private val binding: FragmentRoutineWorkoutBinding,
    private val fragment: Fragment,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val loadRecycledViewPool: RecyclerView.RecycledViewPool
) : RecyclerView.ViewHolder(binding.root) {
    private val context = fragment.requireContext()
    private val viewLifecycleOwner = fragment.viewLifecycleOwner
    private lateinit var viewModel: WorkoutViewModel
    private val sharedViewModel by fragment.sharedViewModel<ExerciseViewModel>()
    private val adapter by lazy { WorkoutExerciseAdapter(
        context = context,
        recycledViewPool = loadRecycledViewPool,
        session = false,
        onClearView = { list -> viewModel.updateAllWorkoutExercise(list.map{ it.exercise }) },
        onMenuClick = showWorkoutExerciseMenuPopup(context, viewModel),
        onLoadClickNested = setButtonEdit(context, viewModel),
        onLoadTextClickNested = setTextEditLoad(context, viewModel)
    )}

    fun bind(item: Workout) {
        viewModel = fragment.get(named("factory")) { parametersOf(fragment, item.id) }
        viewModel.workoutId.value = item.id

        setupRecyclerView()
        subscribeUi()
    }

    private fun setupRecyclerView() {
        adapter.enableDrag()
        adapter.viewLifecycleOwner = fragment.viewLifecycleOwner
        binding.list.adapter = adapter
        binding.list.setRecycledViewPool(recycledViewPool)

        // prevent RecyclerView blinking on submitList
        val animator = binding.list.itemAnimator as SimpleItemAnimator
        animator.supportsChangeAnimations = false

        val helper = ItemTouchHelper(adapter.callback)
        helper.attachToRecyclerView(binding.list)
    }

    private fun subscribeUi() {
        viewModel.exercises.observe(viewLifecycleOwner, Observer { exercises ->
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
        val exercisesWithLoads = exerciseIds.mapIndexed { i, exerciseId ->
            // Make a pair of exercise and their load list
            Pair(
                WorkoutExercise(
                    // Add element at the end with its order in the list of ids
                    i + adapter.itemCount,
                    viewModel.workoutId.value!!,
                    exerciseId
                ),
                listOf(Load(LoadType.WEIGHT, 0F, 0, 0))
            )
        }
        viewModel.insertWorkoutExerciseWithLoads(exercisesWithLoads)
    }
}