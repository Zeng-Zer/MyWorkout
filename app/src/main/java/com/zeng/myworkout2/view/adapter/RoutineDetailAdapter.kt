package com.zeng.myworkout2.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout2.databinding.ListItemWorkoutBinding
import com.zeng.myworkout2.view.WorkoutItem

class RoutineDetailAdapter : ListAdapter<WorkoutItem, RecyclerView.ViewHolder>(WorkoutItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemWorkoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return RoutineDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RoutineDetailViewHolder).bind(item)
    }

    inner class RoutineDetailViewHolder(private val binding: ListItemWorkoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WorkoutItem) {
            item.binding = binding
            item.init()
        }
    }
}

class WorkoutItemDiffCallback : DiffUtil.ItemCallback<WorkoutItem>() {
    override fun areItemsTheSame(oldItem: WorkoutItem, newItem: WorkoutItem): Boolean {
        return oldItem.workoutId == newItem.workoutId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WorkoutItem, newItem: WorkoutItem): Boolean {
        return oldItem == newItem
    }
}
