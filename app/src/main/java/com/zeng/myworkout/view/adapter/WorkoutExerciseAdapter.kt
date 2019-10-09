package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemWorkoutExerciseBinding
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.util.DraggableListAdapter
import com.zeng.myworkout.view.holder.WorkoutExerciseViewHolder

class WorkoutExerciseAdapter(
    private val context: Context,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val isSession: Boolean = false
) : DraggableListAdapter<WorkoutExerciseDetail>(WorkoutExerciseDiffCallback()) {

    init {
        enableDrag()
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition

        val updatedList = currentList.toMutableList()

        updatedList[from].order = to
        updatedList[to].order = from
        updatedList[from] = updatedList[to].also { updatedList[to] = updatedList[from] }

        submitList(updatedList)
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
//        viewModel.updateAllWorkoutExercise(currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WorkoutExerciseViewHolder(
            context,
            recycledViewPool,
            isSession,
            ListItemWorkoutExerciseBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as WorkoutExerciseViewHolder).bind(item)
    }

}

class WorkoutExerciseDiffCallback : DiffUtil.ItemCallback<WorkoutExerciseDetail>() {
    override fun areItemsTheSame(oldItem: WorkoutExerciseDetail, newItem: WorkoutExerciseDetail): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WorkoutExerciseDetail, newItem: WorkoutExerciseDetail): Boolean {
        return oldItem == newItem
    }

}