package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemStatBinding
import com.zeng.myworkout.model.WorkoutWithExercises
import java.text.SimpleDateFormat

class HistoryStatAdapter(
    private val context: Context
) : ListAdapter<WorkoutWithExercises, RecyclerView.ViewHolder>(HistoryHistoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StatViewHolder(ListItemStatBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as StatViewHolder).bind(item)
    }

    private inner class StatViewHolder(val binding: ListItemStatBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(item: WorkoutWithExercises) {
            val format = SimpleDateFormat("dd/MM/yyyy")
            binding.apply {
                routine.text = item.routine ?: "No Routine"
                workout.text = item.workout.name
                date.text = format.format(item.workout.finishDate!!)
                executePendingBindings()
            }

            setupRecyclerView(item)
        }

        private fun setupRecyclerView(item: WorkoutWithExercises) {
            val adapter = HistoryItemAdapter(context)
            binding.list.adapter = adapter
            adapter.submitList(item.exercises)
        }
    }

}