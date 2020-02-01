package com.zeng.myworkout.logic

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.zeng.myworkout.R
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.util.DialogUtils
import com.zeng.myworkout.view.ExerciseSettingsFragmentDirections
import com.zeng.myworkout.viewmodel.WorkoutViewModel
import kotlinx.coroutines.launch

fun showWorkoutExerciseMenuPopup(context: Context, viewModel: WorkoutViewModel, navController: NavController): (View, WorkoutExerciseDetail, Int) -> Unit {
    return { menuView: View, item: WorkoutExerciseDetail, loadSize: Int ->
        val popup = PopupMenu(context, menuView)
        popup.menuInflater.inflate(R.menu.workout_exercise_popup_menu, popup.menu)
        if (loadSize <= 1) {
            popup.menu.removeItem(R.id.remove_set)
        }
        popup.setOnMenuItemClickListener { menuItem ->
            val exercise = item.exercise
            val loads = item.exercise.loads
            val detail = item.detail
            when (menuItem.itemId) {
                R.id.add_set -> {
                    val newExercise = exercise.copy(loads = loads + (loads.lastOrNull() ?: Load()))
                    viewModel.updateWorkoutExercise(newExercise)
                    true
                }
                R.id.remove_set -> {
                    if (loads.size > 1) {
                        val newExercise = exercise.copy(loads = loads.dropLast(1))
                        viewModel.updateWorkoutExercise(newExercise)
                    }
                    true
                }
                R.id.remove_exercise -> {
                    DialogUtils.openValidationDialog(
                        context = context,
                        message = "Remove ${detail.name} ?",
                        positiveFun = { viewModel.deleteWorkoutExercise(exercise) }
                    )
                    true
                }
                R.id.navigate_settings -> {
                    viewModel.viewModelScope.launch {
                        val workout = item.exercise.workoutId?.let { viewModel.workoutById(it) }
                        val referenceWorkouts = workout?.routineId?.let { viewModel.referenceWorkoutsByRoutineId(it) }
                        val refWorkout = referenceWorkouts?.find { it.name == workout.name && it.description == workout.description }
                        val exerciseSettingsId = "${item.exercise.exerciseId ?: "exID"}X${refWorkout?.id ?: "woID"}"
                        val action = ExerciseSettingsFragmentDirections.actionGlobalNavigationExerciseSettings(exerciseSettingsId)
                        navController.navigate(action)
                    }
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}
