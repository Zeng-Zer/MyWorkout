package com.zeng.myworkout.logic

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.zeng.myworkout.R
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.LoadType
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.util.DialogUtils
import com.zeng.myworkout.viewmodel.WorkoutViewModel

fun showWorkoutExerciseMenuPopup(context: Context, viewModel: WorkoutViewModel): (View, WorkoutExerciseDetail, Int) -> Unit {
    return { menuView: View, item: WorkoutExerciseDetail, loadSize: Int ->
        val popup = PopupMenu(context, menuView)
        popup.menuInflater.inflate(R.menu.workout_exercise_popup_menu, popup.menu)
        if (loadSize <= 1) {
            popup.menu.removeItem(R.id.remove_set)
        }
        popup.setOnMenuItemClickListener { menuItem ->
            val exercise = item.exercise
            val loads = item.loads
            val detail = item.detail
            when (menuItem.itemId) {
                R.id.add_set -> {
                    val last = loads.lastOrNull() ?: Load(LoadType.WEIGHT, 0F, 0, 0, exercise.id)
                    val newLoad = last.copy(id = null, order = last.order + 1)
                    viewModel.insertLoad(newLoad)
                    true
                }
                R.id.remove_set -> {
                    if (loads.size > 1) {
                        viewModel.deleteLoad(loads.last())
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
                else -> false
            }
        }
        popup.show()
    }
}
