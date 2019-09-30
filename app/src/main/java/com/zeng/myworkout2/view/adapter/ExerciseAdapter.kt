package com.zeng.myworkout2.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout2.databinding.ListItemExerciseBinding
import com.zeng.myworkout2.model.Exercise

class ExerciseAdapter : ListAdapter<Exercise, RecyclerView.ViewHolder>(ExerciseDiffCallback()) {

    var checkedList: MutableList<Exercise> = mutableListOf()
    val hasChecked: MutableLiveData<Boolean> = MutableLiveData(false)

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
                        checkedList.add(item)
                    } else {
                        checkedList.remove(item)
                    }
                    // update livedata if has checked or not
                    if (hasChecked.value!! != checkedList.isNotEmpty()) {
                        hasChecked.value = checkedList.isNotEmpty()
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
