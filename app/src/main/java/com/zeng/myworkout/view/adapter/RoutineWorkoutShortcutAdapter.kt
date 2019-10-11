package com.zeng.myworkout.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemRoutineWorkoutShortcutBinding
import com.zeng.myworkout.model.Workout

class RoutineWorkoutShortcutAdapter(
    private val context: Context,
    val onWorkoutShortcutClick: (Workout) -> Unit
) : ListAdapter<Workout, RecyclerView.ViewHolder>(WorkoutDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoutineWorkoutShortcutViewHolder(ListItemRoutineWorkoutShortcutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RoutineWorkoutShortcutViewHolder).bind(item)
    }

    inner class RoutineWorkoutShortcutViewHolder(private val binding: ListItemRoutineWorkoutShortcutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Workout) {
            binding.apply {
                workout = item

                button.setOnClickListener { onWorkoutShortcutClick(item) }
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
