package com.zeng.myworkout.view.adapter

import android.content.Context
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.ListItemWorkoutExerciseBinding
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.util.RepositoryUtils
import com.zeng.myworkout.viewmodel.WorkoutExerciseViewModel
import com.zeng.myworkout.viewmodel.getViewModel


class WorkoutExerciseViewHolder(
    private val context: Context,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val binding: ListItemWorkoutExerciseBinding
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var viewModel: WorkoutExerciseViewModel
    private lateinit var adapter: LoadAdapter
    private lateinit var exercise: WorkoutExerciseDetail

    fun bind(item: WorkoutExerciseDetail) {
        exercise = item
        viewModel = getWorkoutExerciseViewModel(item.id!!)
        adapter = LoadAdapter(context, viewModel, item)

        binding.apply {
            exercise = item.detail
            executePendingBindings()
        }

        setupMenuButton()
        setupRecyclerView()
        subscribeUi()
    }

    private fun setupMenuButton() {
        binding.buttonMenu.setOnClickListener {
            showMenuPopup()
        }
    }

    private fun showMenuPopup() {
        val popup = PopupMenu(context, binding.buttonMenu)
        popup.menuInflater.inflate(R.menu.workout_exercise_popup_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_set -> {
                    true
                }
                R.id.remove_set -> {
                    true
                }
                R.id.remove_exercise -> {
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun setupRecyclerView() {
        binding.list.setRecycledViewPool(recycledViewPool)
        binding.list.layoutManager = GridLayoutManager(context, context.resources.getInteger(R.integer.grid_load_row_count))
        binding.list.adapter = adapter
    }

    private fun subscribeUi() {
        viewModel.loads.observe(context as LifecycleOwner, Observer { loads ->
            adapter.submitList(loads)
        })
    }

    private fun getWorkoutExerciseViewModel(workoutExerciseId: Long): WorkoutExerciseViewModel {
        val workoutRepo = RepositoryUtils.getWorkoutRepository(context)
        return (context as AppCompatActivity).getViewModel({
            WorkoutExerciseViewModel(
                workoutRepo,
                workoutExerciseId
            )
        }, workoutExerciseId.toString())
    }
}