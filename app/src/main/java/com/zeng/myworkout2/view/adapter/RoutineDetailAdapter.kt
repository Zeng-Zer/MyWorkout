package com.zeng.myworkout2.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout2.databinding.ListItemWorkoutBinding
import com.zeng.myworkout2.util.RepositoryUtils
import com.zeng.myworkout2.view.WorkoutItem
import com.zeng.myworkout2.viewmodel.BaseViewModelFactory
import com.zeng.myworkout2.viewmodel.WorkoutViewModel

class RoutineDetailAdapter : ListAdapter<WorkoutItem, RecyclerView.ViewHolder>(WorkoutItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemWorkoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return RoutineDetailViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RoutineDetailViewHolder).bind(item)
    }

    inner class RoutineDetailViewHolder(private val context: Context, private val binding: ListItemWorkoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WorkoutItem) {
            val workoutRepository = RepositoryUtils.getWorkoutRepository(context)

            val viewModel: WorkoutViewModel =
                ViewModelProviders.of(context as FragmentActivity, BaseViewModelFactory {
                    WorkoutViewModel(workoutRepository, item.workoutId)
                }).get(item.workoutId.toString(), WorkoutViewModel::class.java)

            item.binding = binding
            item.viewModel = viewModel
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
