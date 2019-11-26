package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemGridLoadBinding
import com.zeng.myworkout.logic.setLoadButton
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.util.weightToString

class LoadAdapter(
    private val context: Context,
    private val session: Boolean,
    private val exercise: WorkoutExerciseDetail,
    private val onLoadClick: (View, Int, WorkoutExerciseDetail) -> Unit,
    private val onLoadTextClick: (View, Load, WorkoutExerciseDetail) -> Unit,
    private val customSession: Boolean = false
) : ListAdapter<Load, RecyclerView.ViewHolder>(LoadDiffCallback()) {

    private val inflater = LayoutInflater.from(context)
    private var position = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LoadViewHolder(ListItemGridLoadBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        this.position = position
        val item = getItem(position)
        (holder as LoadViewHolder).bind(item)
    }

    inner class LoadViewHolder(private val binding: ListItemGridLoadBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Load) {
            binding.apply {
                // Set initial values
                setLoadButton(item, button, session, context.resources)
                value.setText(item.value.weightToString())

                setupCallbacks(item)
                executePendingBindings()
            }

        }

        private fun ListItemGridLoadBinding.setupCallbacks(load: Load) {
            button.setOnClickListener {
                onLoadClick(it, position, exercise)
            }
            value.setOnClickListener {
                onLoadTextClick(it, load, exercise)
            }
        }
    }

}

// TODO REMOVE
class LoadDiffCallback : DiffUtil.ItemCallback<Load>() {
    override fun areItemsTheSame(oldItem: Load, newItem: Load): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Load, newItem: Load): Boolean {
        return oldItem == newItem
    }

}
