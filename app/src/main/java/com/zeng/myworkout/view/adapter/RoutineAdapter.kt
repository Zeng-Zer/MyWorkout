package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemRoutineBinding
import com.zeng.myworkout.model.Routine
import com.zeng.myworkout.util.DraggableListAdapter
import com.zeng.myworkout.viewmodel.RoutineViewModel

class RoutineAdapter(
    private val viewModel: RoutineViewModel,
    private val showRoutineDetailActivity: (Long, Boolean) -> Unit,
    private val deleteRoutine: (Routine) -> Unit
) : DraggableListAdapter<Routine>(RoutineDiffCallback()) {

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
        viewModel.updateRoutine(currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        return RoutineViewHolder(
            context,
            ListItemRoutineBinding.inflate(LayoutInflater.from(context), parent, false),
            showRoutineDetailActivity,
            deleteRoutine
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RoutineViewHolder).bind(item)
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