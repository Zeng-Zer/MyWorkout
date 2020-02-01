package com.zeng.myworkout.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemChartBinding
import com.zeng.myworkout.layoutmodel.ExerciseLineChart
import com.zeng.myworkout.viewmodel.WorkoutExercisesByName

class ExerciseChartAdapter(
    val context: Context
) : ListAdapter<WorkoutExercisesByName, RecyclerView.ViewHolder>(WorkoutExercisesByNameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChartItemViewHolder(ListItemChartBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ChartItemViewHolder).bind(item)
    }

    private inner class ChartItemViewHolder(val binding: ListItemChartBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var lineChart : ExerciseLineChart
        fun bind(item: WorkoutExercisesByName) {
            binding.apply {
                name.text = item.first
                setupChart(item)
                executePendingBindings()
            }
        }

        private fun ListItemChartBinding.setupChart(item: WorkoutExercisesByName) {
            lineChart = ExerciseLineChart(chart)
            lineChart.submitWorkoutExercises(item)
        }

    }

}
