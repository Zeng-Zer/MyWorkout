package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemGridLoadBinding
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.viewmodel.WorkoutExerciseViewModel
import java.text.DecimalFormat
import kotlin.math.roundToInt

class LoadAdapter(
    private val viewModel: WorkoutExerciseViewModel,
    private val exercise: WorkoutExerciseDetail
) : ListAdapter<Load, RecyclerView.ViewHolder>(LoadDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        return LoadViewHolder(ListItemGridLoadBinding.inflate(
            LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as LoadViewHolder).bind(item)
    }

    inner class LoadViewHolder(private val binding: ListItemGridLoadBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Load) {
            binding.apply {
                load = item
                holder = this@LoadViewHolder
                executePendingBindings()
            }

            binding.button.setOnClickListener {
                item.reps -= 1
                binding.button.text = item.reps.toString()
                viewModel.updateLoad(item)
            }
        }

        @SuppressLint("SetTextI18n")
        fun weightToText(weight: Float): String {
            return if (weight.roundToInt().toFloat() == weight) {
                weight.toInt().toString() + "kg"
            } else {
                DecimalFormat("#.##").format(weight) + "kg"
            }
        }
    }

}

class LoadDiffCallback : DiffUtil.ItemCallback<Load>() {
    override fun areItemsTheSame(oldItem: Load, newItem: Load): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Load, newItem: Load): Boolean {
        return oldItem == newItem
    }

}
