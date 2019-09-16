package com.zeng.myworkout2.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout2.databinding.ListItemRoutineBinding
import com.zeng.myworkout2.model.Routine
import com.zeng.myworkout2.viewmodel.RoutineViewModel

class RoutineAdapter(
    private val viewModel: RoutineViewModel,
    private val handleRoutineDetail: (Routine, Boolean) -> Unit
) : ListAdapter<Routine, RecyclerView.ViewHolder>(RoutineDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoutineViewHolder(ListItemRoutineBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RoutineViewHolder).bind(item)
    }

    inner class RoutineViewHolder(private val binding: ListItemRoutineBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.delete.setOnClickListener {
                binding.routine?.let {
                    viewModel.deleteRoutine(it)
                }
            }

            binding.card.setOnClickListener {
                binding.routine?.let {
                    handleRoutineDetail(it, false)
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