package com.zeng.myworkout.view.adapter

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.ListItemRoutineBinding
import com.zeng.myworkout.model.Routine
import com.zeng.myworkout.util.RepositoryUtils
import com.zeng.myworkout.viewmodel.RoutineWorkoutShortcutViewModel
import com.zeng.myworkout.viewmodel.getViewModel


class RoutineViewHolder(
    val context: Context,
    val binding: ListItemRoutineBinding,
    private val showRoutineDetailActivity: (Long, Boolean) -> Unit,
    private val deleteRoutine: (Routine) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var viewModel: RoutineWorkoutShortcutViewModel
    private lateinit var adapter: RoutineWorkoutShortcutAdapter
    private val lifecycleOwner = context as LifecycleOwner
    private lateinit var routine: Routine

    fun bind(item: Routine) {
        viewModel = getRoutineWorkoutShortcutViewModel(item.id!!)
        adapter = RoutineWorkoutShortcutAdapter()
        routine = item

        binding.apply {
            routine = item

            buttonMenu.setOnClickListener { showMenuPopup(it) }

            card.setOnClickListener {
                routine?.let { routine ->
                    showRoutineDetailActivity(routine.id!!, false)
                }
            }

            list.adapter = adapter
        }

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.workouts.observe(lifecycleOwner, Observer { workouts ->
            adapter.submitList(workouts)
        })

        viewModel.hasWorkout.observe(lifecycleOwner, Observer { hasWorkout ->
            if (hasWorkout) {
                binding.workoutShortcutLayout.visibility = View.VISIBLE
            } else {
                binding.workoutShortcutLayout.visibility = View.GONE
            }
        })
    }

    private fun showMenuPopup(viewMenu: View) {
        val popup = PopupMenu(context, viewMenu)
        popup.menuInflater.inflate(R.menu.routine_popup_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete_routine -> {
                    deleteRoutine(routine)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun getRoutineWorkoutShortcutViewModel(routineId: Long): RoutineWorkoutShortcutViewModel {
        val workoutRepo = RepositoryUtils.getWorkoutRepository(context)
        return (context as AppCompatActivity).getViewModel({
            RoutineWorkoutShortcutViewModel(
                workoutRepo,
                routineId
            )
        }, routineId.toString())
    }
}
