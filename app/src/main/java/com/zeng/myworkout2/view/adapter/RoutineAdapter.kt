package com.zeng.myworkout2.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout2.databinding.ListItemRoutineBinding
import com.zeng.myworkout2.model.Routine

class RoutineAdapter : ListAdapter<Routine, RecyclerView.ViewHolder>(RoutineDiffCallback()) {

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
            binding.card.setOnClickListener{
                binding.routine?.workouts
//                binding.routine?.
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