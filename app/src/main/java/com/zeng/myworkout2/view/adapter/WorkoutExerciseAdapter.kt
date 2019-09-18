package com.zeng.myworkout2.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout2.databinding.ListItemWorkoutExerciseBinding
import com.zeng.myworkout2.model.WorkoutExercise
import com.zeng.myworkout2.viewmodel.WorkoutViewModel
import java.text.DecimalFormat
import kotlin.math.roundToInt

class WorkoutExerciseAdapter(private val viewModel: WorkoutViewModel) : ListAdapter<WorkoutExercise, RecyclerView.ViewHolder>(WorkoutExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WorkoutExerciseViewHolder(ListItemWorkoutExerciseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as WorkoutExerciseViewHolder).bind(item)
    }

    inner class WorkoutExerciseViewHolder(private val binding: ListItemWorkoutExerciseBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
//            binding.delete.setOnClickListener {
//                binding.routine?.let {
////                    viewModel.deleteRoutine(it)
//                }
//            }
//
//            binding.card.setOnClickListener {
//                binding.routine?.let {
////                    handleRoutineDetail(it, false)
//                }
//            }
        }

        fun bind(item: WorkoutExercise) {
            binding.apply {
                exercise = item
                weightToText(item)
                executePendingBindings()
            }
        }

        private fun weightToText(ex: WorkoutExercise) {
            if (ex.weight.roundToInt().toFloat() == ex.weight) {
                binding.weight.text = ex.weight.toInt().toString() + "kg"
            } else {
                binding.weight.text = DecimalFormat("#.##").format(ex.weight) + "kg"
            }
        }
    }


}

class WorkoutExerciseDiffCallback : DiffUtil.ItemCallback<WorkoutExercise>() {
    override fun areItemsTheSame(oldItem: WorkoutExercise, newItem: WorkoutExercise): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WorkoutExercise, newItem: WorkoutExercise): Boolean {
        return oldItem == newItem
    }

}