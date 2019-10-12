package com.zeng.myworkout.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.FragmentRoutineWorkoutBinding
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.view.RoutineWorkoutViewHolder

class RoutineWorkoutAdapter(
    private val fragment: Fragment
) : ListAdapter<Workout, RecyclerView.ViewHolder>(WorkoutDiffCallback()) {

    private val recycledViewPool = RecyclerView.RecycledViewPool()
    private val loadRecycledViewPool = RecyclerView.RecycledViewPool()

    fun swapElements(from: Int, to: Int, onSwap: (List<Workout>) -> Unit) {
        val updatedList = currentList.toMutableList()
        updatedList[from].order = to
        updatedList[to].order = from
        updatedList[from] = updatedList[to].also { updatedList[to] = updatedList[from] }
        onSwap(updatedList)
        submitList(updatedList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = FragmentRoutineWorkoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return RoutineWorkoutViewHolder(binding, fragment, recycledViewPool, loadRecycledViewPool)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RoutineWorkoutViewHolder).bind(item)
    }

}
