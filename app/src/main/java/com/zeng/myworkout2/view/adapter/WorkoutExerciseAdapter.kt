package com.zeng.myworkout2.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout2.databinding.WorkoutExerciseItemBinding
import com.zeng.myworkout2.util.DraggableAdapter
import com.zeng.myworkout2.viewmodel.HomeViewModel

class WorkoutExerciseAdapter(private val viewModel: HomeViewModel)
    : DraggableAdapter<WorkoutExerciseAdapter.WorkoutExerciseViewHolder>() {

    inner class WorkoutExerciseViewHolder(private val binding: WorkoutExerciseItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutExerciseViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(
        holder: WorkoutExerciseViewHolder,
        position: Int
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) {
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }
}
