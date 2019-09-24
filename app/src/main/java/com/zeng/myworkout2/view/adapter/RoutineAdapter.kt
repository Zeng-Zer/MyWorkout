package com.zeng.myworkout2.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout2.databinding.ListItemRoutineBinding
import com.zeng.myworkout2.model.Routine
import com.zeng.myworkout2.util.DraggableListAdapter
import com.zeng.myworkout2.viewmodel.RoutineViewModel

class RoutineAdapter(
    private val viewModel: RoutineViewModel,
    private val showRoutineDetailActivity: (Long, Boolean) -> Unit,
    private val deleteRoutineWithUndo: (RoutineViewHolder) -> Unit
) : DraggableListAdapter<Routine>(RoutineDiffCallback()) {

    init {
        enableDrag()
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition

        // IS THERE A BETTER SOLUTION ?
        val updatedList = currentList.toMutableList()

        updatedList[from].order = to
        updatedList[to].order = from
        updatedList[from] = updatedList[to].also { updatedList[to] = updatedList[from] }

        submitList(updatedList)
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        viewModel.updateRoutineSql(currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoutineViewHolder(ListItemRoutineBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RoutineViewHolder).bind(item)
    }

    inner class RoutineViewHolder(val binding: ListItemRoutineBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.delete.setOnClickListener {
                deleteRoutineWithUndo(this)
            }

            binding.card.setOnClickListener {
                binding.routine?.let { routine ->
                    showRoutineDetailActivity(routine.id!!, false)
                }
            }
        }

        fun bind(item: Routine) {
            binding.apply {
                routine = item
                executePendingBindings()
            }
        }
    }

}

class RoutineDiffCallback : DiffUtil.ItemCallback<Routine>() {
    override fun areItemsTheSame(oldItem: Routine, newItem: Routine): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Routine, newItem: Routine): Boolean {
        return oldItem == newItem
    }

}