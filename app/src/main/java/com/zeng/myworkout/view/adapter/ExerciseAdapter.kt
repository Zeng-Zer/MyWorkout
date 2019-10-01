package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemExerciseBinding
import com.zeng.myworkout.model.Exercise
import com.zeng.myworkout.viewmodel.ExerciseViewModel

class ExerciseAdapter(private val viewModel: ExerciseViewModel) : ListAdapter<Exercise, RecyclerView.ViewHolder>(ExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExerciseViewHolder(
            ListItemExerciseBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ExerciseViewHolder).bind(item)
    }

    inner class ExerciseViewHolder(private val binding: ListItemExerciseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Exercise) {
            binding.apply {
                exercise = item
                executePendingBindings()

                // added checked element to checkedlist
                checkbox.setOnCheckedChangeListener{ _, checked ->
                    if (checked) {
                        viewModel.checkedList.add(item)
                    } else {
                        viewModel.checkedList.remove(item)
                    }
                    // update livedata if has checked or not
                    if (viewModel.hasChecked.value!! != viewModel.checkedList.isNotEmpty()) {
                        viewModel.hasChecked.value = viewModel.checkedList.isNotEmpty()
                    }
                }
            }
        }
    }

}

class ExerciseDiffCallback : DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem == newItem
    }

}
