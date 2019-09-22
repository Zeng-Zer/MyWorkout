package com.zeng.myworkout2.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout2.R
import com.zeng.myworkout2.databinding.IntegerPickerBinding
import com.zeng.myworkout2.databinding.ListItemWorkoutExerciseBinding
import com.zeng.myworkout2.databinding.NumberPickerBinding
import com.zeng.myworkout2.model.WorkoutExercise
import com.zeng.myworkout2.model.WorkoutExerciseSql
import com.zeng.myworkout2.viewmodel.WorkoutViewModel
import java.text.DecimalFormat
import kotlin.math.roundToInt

class WorkoutExerciseAdapter(private val viewModel: WorkoutViewModel) : ListAdapter<WorkoutExercise, RecyclerView.ViewHolder>(WorkoutExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        return WorkoutExerciseViewHolder(context, ListItemWorkoutExerciseBinding.inflate(
            LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as WorkoutExerciseViewHolder).bind(item)
    }

    inner class WorkoutExerciseViewHolder(private val context: Context, private val binding: ListItemWorkoutExerciseBinding) : RecyclerView.ViewHolder(binding.root) {

        private val inflater: LayoutInflater = LayoutInflater.from(context)

        init {
            binding.numberSet.setOnClickListener {
                var nbSet = binding.numberSet.text.toString().toInt()
                // 1st lambda onValueChange
                // 2nd lambda onValidation
                showIntegerPicker(nbSet, "Number of sets:", { newValue -> nbSet = newValue }) { exercise ->
                    exercise.sets = nbSet
                    viewModel.updateWorkoutExerciseSql(exercise)
                }
            }

            binding.numberRep.setOnClickListener {
                var nbRep = binding.numberRep.text.toString().toInt()
                showIntegerPicker(nbRep, "Number of reps:", { newValue -> nbRep = newValue }) { exercise ->
                    exercise.reps = nbRep
                    viewModel.updateWorkoutExerciseSql(exercise)
                }
            }

            binding.weight.setOnClickListener {
                onWeightClickListener()
            }
        }

        fun bind(item: WorkoutExercise) {
            binding.apply {
                exerciseSql = item
                exercise = item.exercise
                holder = this@WorkoutExerciseViewHolder
                executePendingBindings()
            }
        }

        private fun showIntegerPicker(initialValue: Int,
                                      message: String,
                                      onValueChange: (Int) -> Unit,
                                      onValidation: (WorkoutExerciseSql) -> Unit) {
            val pickerBinding = IntegerPickerBinding.inflate(inflater)

            pickerBinding.picker.minValue = 1
            pickerBinding.picker.maxValue = 100
            pickerBinding.picker.value = initialValue
            pickerBinding.picker.wrapSelectorWheel = true
            pickerBinding.picker.setOnValueChangedListener { _, _, new -> onValueChange(new) }

            val dialog = AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK") { _, _ -> binding.exerciseSql?.apply(onValidation)}
                .setNegativeButton("CANCEL") {  _, _ ->  }
                .setView(pickerBinding.root)
                .create()

            dialog.window?.setDimAmount(0.1f)
            dialog.show()
        }

        @SuppressLint("SetTextI18n")
        fun weightToText(ex: WorkoutExerciseSql): String {
            return if (ex.weight.roundToInt().toFloat() == ex.weight) {
                ex.weight.toInt().toString() + "kg"
            } else {
                DecimalFormat("#.##").format(ex.weight) + "kg"
            }
        }

        private fun onWeightClickListener() {
            var weight = binding.exerciseSql?.weight ?: 0f
            val numberPickerBinding = DataBindingUtil.inflate<NumberPickerBinding>(inflater, R.layout.number_picker, null, false)
            numberPickerBinding.weight = weight
            numberPickerBinding.increase.setOnClickListener {
                weight += 0.5f
                if (weight > 1000f) {
                    weight = 1000f
                }
                numberPickerBinding.weight = weight
            }
            numberPickerBinding.decrease.setOnClickListener {
                weight -= 0.5f
                if (weight < 0f) {
                    weight = 0f
                }
                numberPickerBinding.weight = weight
            }
            numberPickerBinding.number.keyListener = DigitsKeyListener.getInstance(false, true)
            numberPickerBinding.number.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s.isNullOrEmpty()) {
                        weight = 0f
                    } else {
                        if (s.startsWith(".")) {
                            s.insert(0, "0")
                        }
                        weight = s.toString().toFloat()
                        if (weight > 1000f) {
                            weight = 1000f
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            val dialog = android.app.AlertDialog.Builder(context)
                .setMessage("Weight")
                .setPositiveButton("OK") { _, _ ->
                    binding.exerciseSql?.let { exercise ->
                        exercise.weight = weight
                        viewModel.updateWorkoutExerciseSql(exercise)
                    }
                }
                .setNegativeButton("CANCEL") {  _, _ ->  }
                .setView(numberPickerBinding.root)
                .create()

            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            dialog.window?.setDimAmount(0.1f)
            dialog.show()
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