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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.IntegerPickerBinding
import com.zeng.myworkout.databinding.ListItemGridLoadBinding
import com.zeng.myworkout.databinding.NumberPickerBinding
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.WorkoutExerciseDetail
import java.text.DecimalFormat
import kotlin.math.roundToInt

class LoadAdapter(
    private val context: Context,
//    private val viewModel: WorkoutExerciseViewModel,
    private val exercise: WorkoutExerciseDetail,
    private val isSession: Boolean
) : ListAdapter<Load, RecyclerView.ViewHolder>(LoadDiffCallback()) {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LoadViewHolder(ListItemGridLoadBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as LoadViewHolder).bind(item)
    }

    inner class LoadViewHolder(private val binding: ListItemGridLoadBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var load: Load
        private val reps = MutableLiveData<Int>()

        fun bind(item: Load) {
            load = item
            binding.apply {
                load = item
                holder = this@LoadViewHolder
                executePendingBindings()
            }

            if (isSession) {
                setButtonSessionReps()
            } else {
                setButtonEditReps()
            }
            setTextEditLoad()
        }

        private fun setButtonEditReps() {
            binding.button.setOnClickListener {
                val pickerBinding = IntegerPickerBinding.inflate(inflater)

                pickerBinding.picker.minValue = 1
                pickerBinding.picker.maxValue = 100
                pickerBinding.picker.value = load.reps
                pickerBinding.picker.wrapSelectorWheel = true
                pickerBinding.picker.setOnValueChangedListener { _, _, new ->
                    load.reps = new
                }

                val dialog = AlertDialog.Builder(context)
                    .setMessage("Number of reps:")
                    .setPositiveButton("OK") { _, _ ->
                        binding.button.text = load.reps.toString()
//                        viewModel.updateLoad(load)
                    }
                    .setNegativeButton("CANCEL") {  _, _ ->  }
                    .setView(pickerBinding.root)
                    .create()

                dialog.window?.setDimAmount(0.1f)
                dialog.show()
            }
        }

        // TODO REFACTOR THIS
        private fun setTextEditLoad() {
            binding.value.setOnClickListener {
                var weight = load.value
                val numberPickerBinding = DataBindingUtil.inflate<NumberPickerBinding>(inflater, R.layout.number_picker, null, false)
                numberPickerBinding.value = weight
                numberPickerBinding.increase.setOnClickListener {
                    weight += 0.5f
                    if (weight > 1000f) {
                        weight = 1000f
                    }
                    numberPickerBinding.value = weight
                }
                numberPickerBinding.decrease.setOnClickListener {
                    weight -= 0.5f
                    if (weight < 0f) {
                        weight = 0f
                    }
                    numberPickerBinding.value = weight
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
                        load.value = weight
                        binding.value.setText(weightToText(weight))
//                        viewModel.updateLoad(load)
                    }
                    .setNegativeButton("CANCEL") {  _, _ ->  }
                    .setView(numberPickerBinding.root)
                    .create()

                dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                dialog.window?.setDimAmount(0.1f)
                dialog.show()
            }
        }

        // TODO SET OTHER TYPE
        private fun setButtonSessionReps() {
            reps.value = load.repsDone
            reps.observe(context as LifecycleOwner, Observer { it?.let { value ->
                if (value == -1) {
                    binding.button.text = load.reps.toString()
                    binding.button.background = context.resources.getDrawable(R.drawable.my_button_unselected)
                } else {
                    binding.button.text = value.toString()
                    binding.button.background = context.resources.getDrawable(R.drawable.my_button_ripple)
                }
            }})

            binding.button.setOnClickListener {
                load.repsDone -= 1

                if (load.repsDone < -1) {
                    load.repsDone = load.reps
                }

                reps.value = load.repsDone
//                viewModel.updateLoad(load)
            }
        }

        @SuppressLint("SetTextI18n")
        fun weightToText(weight: Float): String {
            return if (weight.roundToInt().toFloat() == weight) {
                weight.toInt().toString() + "kg"
            } else {
                DecimalFormat("#.##").format(weight) + "kg"
            }
        }
    }

}

class LoadDiffCallback : DiffUtil.ItemCallback<Load>() {
    override fun areItemsTheSame(oldItem: Load, newItem: Load): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Load, newItem: Load): Boolean {
        return oldItem == newItem
    }

}
