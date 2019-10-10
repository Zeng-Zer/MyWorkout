package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.ListItemWorkoutExerciseBinding
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.util.DraggableListAdapter

class WorkoutExerciseAdapter(
    private val context: Context,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val onClearView: (List<WorkoutExerciseDetail>) -> Unit,
    private val onMenuClick: (View, WorkoutExerciseDetail) -> Unit,
    private val onLoadClickNested: (View, Load) -> Unit,
    private val onLoadTextClickNested: (View, Load) -> Unit,
    private val session: Boolean = false
) : DraggableListAdapter<WorkoutExerciseDetail>(WorkoutExerciseDiffCallback()) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition

        val updatedList = currentList.toMutableList()
        updatedList[from].exercise.order = to
        updatedList[to].exercise.order = from
        updatedList[from] = updatedList[to].also { updatedList[to] = updatedList[from] }

        submitList(updatedList)
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) = onClearView(currentList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WorkoutExerciseViewHolder(ListItemWorkoutExerciseBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as WorkoutExerciseViewHolder).bind(item)
    }

    inner class WorkoutExerciseViewHolder(private val binding: ListItemWorkoutExerciseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WorkoutExerciseDetail) {
            binding.apply {
                exercise = item.detail
                setupAdapter(item.loads)
                setupRecyclerView()
                setupCallbacks(item)
                executePendingBindings()
            }
        }

        private fun ListItemWorkoutExerciseBinding.setupAdapter(loads: List<Load>) {
            val adapter = LoadAdapter(context, session)
            adapter.onLoadClick = onLoadClickNested
            adapter.onLoadTextClick = onLoadTextClickNested
            adapter.submitList(loads)
            list.adapter = adapter
        }


        private fun ListItemWorkoutExerciseBinding.setupRecyclerView() {
            list.setRecycledViewPool(recycledViewPool)
            list.layoutManager = GridLayoutManager(context, context.resources.getInteger(R.integer.grid_load_row_count))
        }

        private fun ListItemWorkoutExerciseBinding.setupCallbacks(item: WorkoutExerciseDetail) {
            buttonMenu.setOnClickListener {
                onMenuClick(it, item)
            }
        }
    }

}

class WorkoutExerciseDiffCallback : DiffUtil.ItemCallback<WorkoutExerciseDetail>() {
    override fun areItemsTheSame(oldItem: WorkoutExerciseDetail, newItem: WorkoutExerciseDetail): Boolean {
        return oldItem.exercise.id == newItem.exercise.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WorkoutExerciseDetail, newItem: WorkoutExerciseDetail): Boolean {
        return oldItem == newItem
    }

}