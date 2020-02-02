package com.zeng.myworkout.layoutmodel

import android.annotation.SuppressLint
import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.zeng.myworkout.logic.getHeaviestLoadWeight
import com.zeng.myworkout.util.SettingsSingleton
import com.zeng.myworkout.viewmodel.DatedWeight
import com.zeng.myworkout.viewmodel.DatedWorkoutExercise
import com.zeng.myworkout.viewmodel.WorkoutExercisesByName
import java.text.SimpleDateFormat

class ExerciseLineChart(private val chart: LineChart) {

    private lateinit var dataset: List<DatedWeight>
    private lateinit var exerciseName: String
    private val PADDING = 0.05f

    fun submitWorkoutExercises(workoutExercisesByName: WorkoutExercisesByName) {
        exerciseName = workoutExercisesByName.first
        initializeData(workoutExercisesByName.second)
        initializeChart()
        chart.invalidate()
    }

    private fun initializeData(dataset: List<DatedWorkoutExercise>) {
        this.dataset = dataset.map { it.first to getHeaviestLoadWeight(it.second.exercise.loads)}
        val entries = this.dataset
            .mapIndexed { i, datedWeight ->
                if (SettingsSingleton.isImperial()) {
                    Entry(i.toFloat(), datedWeight.second * SettingsSingleton.imperialCoef)
                } else {
                    Entry(i.toFloat(), datedWeight.second)
                }
            }
        val lineDataSet = LineDataSet(entries, exerciseName)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        chart.data = LineData(lineDataSet)
    }

    @SuppressLint("SimpleDateFormat")
    private fun initializeChart() {
        chart.xAxis.setDrawAxisLine(true)
        chart.xAxis.setDrawGridLines(true)
        chart.xAxis.textColor = Color.BLACK
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val fmt = SimpleDateFormat("dd MMM")
                val date = dataset[value.toInt()].first
                return fmt.format(date)
            }
        }

        if (SettingsSingleton.isImperial()) {
            chart.axisLeft.axisMinimum = dataset.map{ it.second * SettingsSingleton.imperialCoef }.min()!! * (1f - PADDING)
            chart.axisLeft.axisMaximum = dataset.map{ it.second * SettingsSingleton.imperialCoef }.max()!! * (1f + PADDING)
        } else {
            chart.axisLeft.axisMinimum = dataset.map{ it.second }.min()!! * (1f - PADDING)
            chart.axisLeft.axisMaximum = dataset.map{ it.second }.max()!! * (1f + PADDING)
        }
        chart.axisLeft.textColor = Color.BLACK

        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.legend.isEnabled = false
        chart.setTouchEnabled(false)
    }
}