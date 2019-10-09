package com.zeng.myworkout.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zeng.myworkout.model.WorkoutName
import com.zeng.myworkout.view.RoutineWorkoutFragment

class RoutineWorkoutAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    var currentList: List<WorkoutName> = emptyList()
        private set

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return RoutineWorkoutFragment(currentList[position].id)
    }

    fun submitList(list: List<WorkoutName>, runnable: (() -> Unit)? = null) {
        currentList = list
        notifyDataSetChanged()
        runnable?.invoke()
    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val binding = ListItemWorkoutBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false)
//        return RoutineDetailViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val item = getItem(position)
//        (holder as RoutineDetailViewHolder).bind(item)
//    }
//
//    inner class RoutineDetailViewHolder(private val binding: ListItemWorkoutBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: WorkoutItem) {
//            item.binding = binding
//            item.init()
//        }
//    }
}

//class WorkoutItemDiffCallback : DiffUtil.ItemCallback<WorkoutItem>() {
//    override fun areItemsTheSame(oldItem: WorkoutItem, newItem: WorkoutItem): Boolean {
//        return oldItem.workoutId == newItem.workoutId
//    }
//
//    // TODO maybe we don't need to check everything here
//    @SuppressLint("DiffUtilEquals")
//    override fun areContentsTheSame(oldItem: WorkoutItem, newItem: WorkoutItem): Boolean {
//        return oldItem == newItem
//    }
//}
