package com.zeng.myworkout.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.ListItemRoutineWorkoutShortcutBinding
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.util.DialogUtils
import com.zeng.myworkout.viewmodel.RoutineWorkoutShortcutViewModel
import kotlinx.coroutines.launch

class RoutineWorkoutShortcutAdapter(
    private val context: Context,
    private val viewModel: RoutineWorkoutShortcutViewModel
) : ListAdapter<Workout, RecyclerView.ViewHolder>(WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoutineWorkoutShortcutViewHolder(ListItemRoutineWorkoutShortcutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RoutineWorkoutShortcutViewHolder).bind(item)
    }

    inner class RoutineWorkoutShortcutViewHolder(private val binding: ListItemRoutineWorkoutShortcutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(workout: Workout) {
            binding.workout = workout

            binding.button.setOnClickListener {
                viewModel.viewModelScope.launch {
                    val user = viewModel.getUser()
                    // Check if the user currently has a session
                    if (user?.workoutSessionId != null) {
                        DialogUtils.openValidationDialog(
                            context = context,
                            message = "Close current workout session ?",
                            positiveFun = {
                                viewModel.deleteWorkout(user.workoutSessionId!!)
                                setUserWorkout(workout)
                            }
                        )
                    } else {
                        setUserWorkout(workout)
                    }
                }
            }
        }

        private fun setUserWorkout(workout: Workout) {
            viewModel.viewModelScope.launch {
                viewModel.updateUserWorkout(workout)
                val navController = (context as FragmentActivity).findNavController(R.id.nav_host_fragment)
                navController.navigate(R.id.action_navigation_routine_to_home_nav)
            }
        }
    }

}

class WorkoutDiffCallback : DiffUtil.ItemCallback<Workout>() {
    override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem == newItem
    }

}
