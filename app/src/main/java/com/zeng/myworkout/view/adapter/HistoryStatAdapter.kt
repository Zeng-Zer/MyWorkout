package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemStatBinding
import com.zeng.myworkout.model.WorkoutWithExercises
import com.zeng.myworkout.viewmodel.GroupedWorkouts
import java.text.SimpleDateFormat
import java.util.*

class HistoryStatAdapter(
    private val context: Context
) : ListAdapter<GroupedWorkouts, RecyclerView.ViewHolder>(HistoryStatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StatViewHolder(ListItemStatBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as StatViewHolder).bind(item)
    }

    private inner class StatViewHolder(val binding: ListItemStatBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(item: GroupedWorkouts) {
            val format = SimpleDateFormat("dd/MM/yyyy")
            val routineName = item.first.first
            val workoutName = item.first.second
            val workoutFinishDate = getWorkoutStartDate(item.second)
            binding.apply {
                routine.text = routineName ?: "No Routine"
                workout.text = workoutName
                date.text = format.format(workoutFinishDate)
                executePendingBindings()
            }

//            setupRecyclerView(item)
        }

        private fun setupRecyclerView(item: WorkoutWithExercises) {
            val adapter = HistoryItemAdapter(context)
            binding.list.adapter = adapter
            adapter.submitList(item.exercises)
        }

        private fun getWorkoutStartDate(workouts: List<WorkoutWithExercises>): Date {
            return workouts
                .map { w -> w.workout.startDate!! }
                .reduce { d1, d2 -> if (d1.before(d2)) d1 else d2 }
        }
    }

}

class HistoryStatDiffCallback : DiffUtil.ItemCallback<GroupedWorkouts>() {
    override fun areItemsTheSame(oldItem: GroupedWorkouts, newItem: GroupedWorkouts): Boolean {
        return oldItem.first == newItem.first
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: GroupedWorkouts, newItem: GroupedWorkouts): Boolean {
        return oldItem.second == newItem.second
    }

}
