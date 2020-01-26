package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemStatExerciseBinding
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.util.weightToString
import com.zeng.myworkout.viewmodel.ExercisesByName

class StatItemAdapter(
    private val context: Context
) : ListAdapter<ExercisesByName, RecyclerView.ViewHolder>(StatItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StatItemViewHolder(ListItemStatExerciseBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as StatItemViewHolder).bind(item)
    }

    private inner class StatItemViewHolder(val binding: ListItemStatExerciseBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(item: ExercisesByName) {
            var filteredExercises = item.second.filter { it.exercise.loads.any { it.repsDone != -1 } }
            if (filteredExercises.isEmpty()) {
                filteredExercises = listOf(item.second.last())
            }
            val firstLoads = filteredExercises.first().exercise.loads
            val currentLoads = filteredExercises.last().exercise.loads
            val allLoads = filteredExercises.flatMap { it.exercise.loads }
            binding.apply {
                name.text = item.first
                initialWeight.text = getHeaviestLoadWeight(firstLoads).weightToString()
                currentWeight.text = getHeaviestLoadWeight(currentLoads).weightToString()
                maxWeight.text = getHeaviestLoadWeight(allLoads).weightToString()
                repsDone.text = getRepsDone(allLoads).toString()
            }
        }

        private fun getHeaviestLoadWeight(loads: List<Load>): Float {
            val maxWeight = loads
                .filter { it.repsDone != -1 }
                .map { it.value }
                .max()
            return maxWeight ?: loads.first().value
        }

        private fun getRepsDone(loads: List<Load>): Int {
            return loads
                .map { it.repsDone }
                .filter { it != -1 }
                .sum()
        }
    }

}

class StatItemDiffCallback : DiffUtil.ItemCallback<ExercisesByName>() {
    override fun areItemsTheSame(oldItem: ExercisesByName, newItem: ExercisesByName): Boolean {
        return oldItem.first == newItem.first
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ExercisesByName, newItem: ExercisesByName): Boolean {
        return oldItem.second == newItem.second
    }

}
