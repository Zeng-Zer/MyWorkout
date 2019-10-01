package com.zeng.myworkout.view.adapter

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
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.IntegerPickerBinding
import com.zeng.myworkout.databinding.ListItemWorkoutExerciseBinding
import com.zeng.myworkout.databinding.NumberPickerBinding
import com.zeng.myworkout.model.WorkoutExercise
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.util.DraggableListAdapter
import com.zeng.myworkout.viewmodel.WorkoutViewModel
import java.text.DecimalFormat
import kotlin.math.roundToInt

class WorkoutExerciseAdapter(private val viewModel: WorkoutViewModel) : DraggableListAdapter<WorkoutExerciseDetail>(WorkoutExerciseDiffCallback()) {

    init {
        enableDrag()
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition

        // TODO IS THERE A BETTER SOLUTION ?
        val updatedList = currentList.toMutableList()

        updatedList[from].order = to
        updatedList[to].order = from
        updatedList[from] = updatedList[to].also { updatedList[to] = updatedList[from] }

        submitList(updatedList)
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        viewModel.updateAllWorkoutExercise(currentList)
    }


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
                showIntegerPicker(nbSet, "Number of sets:",
                    // On value change
                    { newValue ->
                        nbSet = newValue
                    },
                    // On validation
                    { exercise ->
                        exercise.sets = nbSet
                        viewModel.updateWorkoutExercise(exercise)
                    }
                )
            }

            binding.numberRep.setOnClickListener {
                var nbRep = binding.numberRep.text.toString().toInt()
                showIntegerPicker(nbRep, "Number of reps:",
                    // On value change
                    { newValue ->
                        nbRep = newValue
                    },
                    // On validation
                    { exercise ->
                        exercise.reps = nbRep
                        viewModel.updateWorkoutExercise(exercise)
                    }
                )
            }

            binding.weight.setOnClickListener {
                onWeightClickListener()
            }
        }

        fun bind(item: WorkoutExerciseDetail) {
            binding.apply {
                exercise = item
                detail = item.detail
                holder = this@WorkoutExerciseViewHolder
                executePendingBindings()
            }
        }

        private fun showIntegerPicker(initialValue: Int,
                                      message: String,
                                      onValueChange: (Int) -> Unit,
                                      onValidation: (WorkoutExercise) -> Unit) {
            val pickerBinding = IntegerPickerBinding.inflate(inflater)

            pickerBinding.picker.minValue = 1
            pickerBinding.picker.maxValue = 100
            pickerBinding.picker.value = initialValue
            pickerBinding.picker.wrapSelectorWheel = true
            pickerBinding.picker.setOnValueChangedListener { _, _, new -> onValueChange(new) }

            val dialog = AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK") { _, _ -> binding.exercise?.let(onValidation)}
                .setNegativeButton("CANCEL") {  _, _ ->  }
                .setView(pickerBinding.root)
                .create()

            dialog.window?.setDimAmount(0.1f)
            dialog.show()
        }

        @SuppressLint("SetTextI18n")
        fun weightToText(ex: WorkoutExercise): String {
            return if (ex.weight.roundToInt().toFloat() == ex.weight) {
                ex.weight.toInt().toString() + "kg"
            } else {
                DecimalFormat("#.##").format(ex.weight) + "kg"
            }
        }

        // TODO PROBABLY NEED TO REFACTOR THIS
        private fun onWeightClickListener() {
            var weight = binding.exercise?.weight ?: 0f
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
                    binding.exercise?.let { exercise ->
                        exercise.weight = weight
                        viewModel.updateWorkoutExercise(exercise)
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

class WorkoutExerciseDiffCallback : DiffUtil.ItemCallback<WorkoutExerciseDetail>() {
    override fun areItemsTheSame(oldItem: WorkoutExerciseDetail, newItem: WorkoutExerciseDetail): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WorkoutExerciseDetail, newItem: WorkoutExerciseDetail): Boolean {
        return oldItem == newItem
    }

}