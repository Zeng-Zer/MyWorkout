package com.zeng.myworkout.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemRoutineBinding
import com.zeng.myworkout.model.Routine
import com.zeng.myworkout.model.RoutineWithWorkouts
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.util.DraggableListAdapter

class RoutineAdapter(private val context: Context) : DraggableListAdapter<RoutineWithWorkouts>(RoutineWithWorkoutsDiffCallback()) {

    lateinit var onClearView: (List<RoutineWithWorkouts>) -> Unit
    lateinit var onItemClick: (Long, Boolean) -> Unit
    lateinit var onMenuClick: (View, Routine) -> Unit

    // Nested
    lateinit var onWorkoutShortcutClickNested: (Workout) -> Unit

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition

        val updatedList = currentList.toMutableList()
        updatedList[from].routine.order = to
        updatedList[to].routine.order = from
        updatedList[from] = updatedList[to].also { updatedList[to] = updatedList[from] }

        submitList(updatedList)
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) = onClearView(currentList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoutineViewHolder(ListItemRoutineBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RoutineViewHolder).bind(item)
    }

    private inner class RoutineViewHolder(val binding: ListItemRoutineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RoutineWithWorkouts) {
            binding.apply {
                setupAdapter(item.workouts)
                setCallbacks(item.routine)
                routine = item.routine
                executePendingBindings()
            }
        }

        private fun ListItemRoutineBinding.setupAdapter(workouts: List<Workout>) {
            if (workouts.isNotEmpty()) {
                val adapter = RoutineWorkoutShortcutAdapter(context)
                adapter.submitList(workouts)
                adapter.onWorkoutShortcutClick = onWorkoutShortcutClickNested
                list.adapter = adapter
            } else {
                // Hide divider if there isn't any workouts
                workoutShortcutLayout.visibility = View.GONE
            }
        }

        private fun ListItemRoutineBinding.setCallbacks(routine: Routine) {
            // Show popup menu
            buttonMenu.setOnClickListener {
                onMenuClick(it, routine)
            }

            // Navigate to routine details
            card.setOnClickListener {
                onItemClick(routine.id!!, false)
            }
        }
    }
}

class RoutineWithWorkoutsDiffCallback : DiffUtil.ItemCallback<RoutineWithWorkouts>() {
    override fun areItemsTheSame(oldItem: RoutineWithWorkouts, newItem: RoutineWithWorkouts): Boolean {
        return oldItem.routine.id == newItem.routine.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: RoutineWithWorkouts, newItem: RoutineWithWorkouts): Boolean {
        return oldItem.routine == newItem.routine
    }

}