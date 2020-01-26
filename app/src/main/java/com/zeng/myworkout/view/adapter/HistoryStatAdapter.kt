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
import com.zeng.myworkout.util.weightToString
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
            val totalWeight = item.second.fold(0f) { acc, it -> acc + getWeightLifted(it)}.weightToString()
            binding.apply {
                routine.text = routineName ?: "No Routine"
                workout.text = workoutName
                date.text = format.format(workoutFinishDate)
                weightLifted.text = totalWeight
                nbSessions.text = item.second.count().toString()
                executePendingBindings()
            }

            setupRecyclerView(item.second)
        }

        private fun setupRecyclerView(workouts: List<WorkoutWithExercises>) {
            val exercises = workouts
                .flatMap { w ->
                    w.exercises.map { e -> w.workout.finishDate!! to e }
                }
                .groupBy { ex -> ex.second.detail.name }
                .mapValues { exs ->
                    exs.value
                        .sortedBy { it.first }
                        .map { it.second }
                }
                .toList()

            val adapter = StatItemAdapter(context)
            binding.list.adapter = adapter
            adapter.submitList(exercises)
        }

        private fun getWorkoutStartDate(workouts: List<WorkoutWithExercises>): Date {
            return workouts
                .map { w -> w.workout.startDate!! }
                .reduce { d1, d2 -> if (d1.before(d2)) d1 else d2 }
        }

        private fun getWeightLifted(workout: WorkoutWithExercises): Float {
            return workout.exercises
                .fold(0f) { acc, e ->
                    e.exercise.loads.fold(acc) { a, l -> a + l.repsDone * l.value }
                }
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
