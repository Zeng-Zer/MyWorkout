package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.FragmentRoutineWorkoutBinding
import com.zeng.myworkout.model.WorkoutName
import com.zeng.myworkout.view.RoutineWorkoutViewHolder

class RoutineWorkoutAdapter(
    private val fragment: Fragment
) : ListAdapter<WorkoutName, RecyclerView.ViewHolder>(WorkoutNameDiffCallback()) {

    private val recycledViewPool = RecyclerView.RecycledViewPool()
    private val loadRecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = FragmentRoutineWorkoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return RoutineWorkoutViewHolder(binding, fragment, recycledViewPool, loadRecycledViewPool)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RoutineWorkoutViewHolder).bind(item)
    }

}

class WorkoutNameDiffCallback : DiffUtil.ItemCallback<WorkoutName>() {
    override fun areItemsTheSame(oldItem: WorkoutName, newItem: WorkoutName): Boolean {
        return oldItem.id == newItem.id
    }

    // TODO maybe we don't need to check everything here
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WorkoutName, newItem: WorkoutName): Boolean {
        return oldItem == newItem
    }
}
