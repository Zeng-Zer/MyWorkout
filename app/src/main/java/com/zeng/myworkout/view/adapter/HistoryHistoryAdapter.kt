package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemHistoryBinding
import com.zeng.myworkout.model.WorkoutWithExercises
import java.text.SimpleDateFormat

class HistoryHistoryAdapter(
    private val context: Context
) : ListAdapter<WorkoutWithExercises, RecyclerView.ViewHolder>(HistoryHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HistoryViewHolder(ListItemHistoryBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as HistoryViewHolder).bind(item)
    }

    private inner class HistoryViewHolder(val binding: ListItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
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

class HistoryHistoryDiffCallback : DiffUtil.ItemCallback<WorkoutWithExercises>() {
    override fun areItemsTheSame(oldItem: WorkoutWithExercises, newItem: WorkoutWithExercises): Boolean {
        return oldItem.workout.id == newItem.workout.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WorkoutWithExercises, newItem: WorkoutWithExercises): Boolean {
        return oldItem == newItem
    }

}