package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemStatExerciseBinding
import com.zeng.myworkout.logic.getHeaviestLoadWeight
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.util.weightToString
import com.zeng.myworkout.viewmodel.WorkoutExercisesByName

class StatItemAdapter(
    private val context: Context
) : ListAdapter<WorkoutExercisesByName, RecyclerView.ViewHolder>(WorkoutExercisesByNameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StatItemViewHolder(ListItemStatExerciseBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as StatItemViewHolder).bind(item)
    }

    private inner class StatItemViewHolder(val binding: ListItemStatExerciseBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(item: WorkoutExercisesByName) {
            var filteredExercises = item.second.filter { it.second.exercise.loads.any { it.repsDone != -1 } }
            if (filteredExercises.isEmpty()) {
                filteredExercises = listOf(item.second.last())
            }
            val firstLoads = filteredExercises.first().second.exercise.loads
            val currentLoads = filteredExercises.last().second.exercise.loads
            val allLoads = filteredExercises.flatMap { it.second.exercise.loads }
            binding.apply {
                name.text = item.first
                initialWeight.text = getHeaviestLoadWeight(firstLoads).weightToString()
                currentWeight.text = getHeaviestLoadWeight(currentLoads).weightToString()
                maxWeight.text = getHeaviestLoadWeight(allLoads).weightToString()
                repsDone.text = getRepsDone(allLoads).toString()
            }
        }

        private fun getRepsDone(loads: List<Load>): Int {
            return loads
                .map { it.repsDone }
                .filter { it != -1 }
                .sum()
        }
    }

}

class WorkoutExercisesByNameDiffCallback : DiffUtil.ItemCallback<WorkoutExercisesByName>() {
    override fun areItemsTheSame(oldItem: WorkoutExercisesByName, newItem: WorkoutExercisesByName): Boolean {
        return oldItem.first == newItem.first
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WorkoutExercisesByName, newItem: WorkoutExercisesByName): Boolean {
        return oldItem.second == newItem.second
    }

}
