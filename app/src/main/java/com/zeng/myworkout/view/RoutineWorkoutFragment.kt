package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.FragmentRoutineWorkoutBinding
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.util.DialogUtils
import com.zeng.myworkout.view.adapter.WorkoutExerciseAdapter
import com.zeng.myworkout.viewmodel.RoutineWorkoutViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RoutineWorkoutFragment(private val workoutId: Long) : Fragment() {

    private lateinit var binding: FragmentRoutineWorkoutBinding
    private val viewModel by viewModel<RoutineWorkoutViewModel> { parametersOf(workoutId) }
    private val exerciseRecycledViewPool = RecyclerView.RecycledViewPool()
    private val adapter by lazy { WorkoutExerciseAdapter(requireContext(), exerciseRecycledViewPool) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRoutineWorkoutBinding.inflate(inflater, container, false)

        setupAdapter()
        setupRecyclerView()
        subscribeUi()
        return binding.root
    }

    private fun setupAdapter() {
        adapter.enableDrag()
        adapter.onMenuClick = this::showWorkoutExerciseMenuPopup
        adapter.onClearView = { list -> viewModel.updateAllWorkoutExercise(list.map{ it.exercise }) }
    }

    private fun setupRecyclerView() {
//        binding.list.setRecycledViewPool(workoutRecycledViewPool)
        binding.list.adapter = adapter

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
    }

    private fun showWorkoutExerciseMenuPopup(menuView: View, exercise: WorkoutExerciseDetail) {
        val popup = PopupMenu(requireContext(), menuView)
        popup.menuInflater.inflate(R.menu.workout_exercise_popup_menu, popup.menu)
        if (adapter.currentList.size <= 1) {
            popup.menu.removeItem(R.id.remove_set)
        }
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_set -> {
//                    viewModel.loads.value?.let { loads ->
//                        val last = loads.lastOrNull() ?: Load(LoadType.WEIGHT, 0F, 0, 0, exercise.id)
//                        val newLoad = last.copy(id = null, order = last.order + 1)
//                        viewModel.insertLoad(newLoad)
//                    }
                    true
                }
                R.id.remove_set -> {
//                    viewModel.loads.value?.let { loads ->
//                        if (loads.size > 1) {
//                            viewModel.deleteLoad(loads.last())
//                        }
//                    }
                    true
                }
                R.id.remove_exercise -> {
                    DialogUtils.openValidationDialog(
                        context = requireContext(),
                        message = "Remove ${exercise.detail.name} ?",
                        positiveFun = { viewModel.deleteWorkoutExercise(exercise.exercise) }
                    )
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}