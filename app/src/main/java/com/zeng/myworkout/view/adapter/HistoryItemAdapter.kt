package com.zeng.myworkout.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemHistoryExerciseBinding
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.util.weightToString

class HistoryItemAdapter(
    val context: Context
) : ListAdapter<WorkoutExerciseDetail, RecyclerView.ViewHolder>(WorkoutExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HistoryItemViewHolder(ListItemHistoryExerciseBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as HistoryItemViewHolder).bind(item)
    }

    private inner class HistoryItemViewHolder(val binding: ListItemHistoryExerciseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WorkoutExerciseDetail) {
            binding.apply {
                name.text = item.detail.name

                val setsDetail = item.exercise.loads.fold("") { acc, load ->
                    if (load.repsDone == -1) {
                        acc
                    } else {
                        acc + "${load.repsDone}x${load.value.weightToString()}   "
                    }
                }
                sets.text = if (setsDetail.isEmpty()) { "Skipped" } else { setsDetail }
                executePendingBindings()
            }
        }

    }

}
