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
import com.zeng.myworkout.viewmodel.WorkoutExercisesByName
import java.text.SimpleDateFormat
import java.util.*

class HistoryStatAdapter(
    private val context: Context,
    private val onItemClick: (String, String, List<WorkoutExercisesByName>) -> Unit
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
            val exercisesByName = getExercisesByNames(item.second)
            val format = SimpleDateFormat("dd/MM/yyyy")
            val routineName = item.first.first ?: "No Routine"
            val workoutName = item.first.second
            val workoutFinishDate = getWorkoutStartDate(item.second)
            val totalWeight = item.second.fold(0f) { acc, it -> acc + getWeightLifted(it)}.weightToString()
            binding.apply {
                routine.text = routineName
                workout.text = workoutName
                date.text = format.format(workoutFinishDate)
                weightLifted.text = totalWeight
                nbSessions.text = item.second.count().toString()
                // Navigate to charts
                card.setOnClickListener {
                    onItemClick(routineName, workoutName, exercisesByName)
                }

                executePendingBindings()
            }

            setupRecyclerView(exercisesByName)
        }

        private fun getExercisesByNames(workouts: List<WorkoutWithExercises>): List<WorkoutExercisesByName> {
            return workouts
                .flatMap { w ->
                    w.exercises.map { e -> w.workout.finishDate!! to e }
                }
                .groupBy { ex -> ex.second.detail.name }
                .mapValues { exs ->
                    exs.value
                        .sortedBy { it.first }
                }
                .toList()
        }

        private fun setupRecyclerView(exercises: List<WorkoutExercisesByName>) {
            val adapter = StatItemAdapter(context)
            binding.list.adapter = adapter
            binding.list.suppressLayout(true)
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
