package com.zeng.myworkout.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.ListItemWorkoutExerciseBinding
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.util.RepositoryUtils
import com.zeng.myworkout.view.adapter.LoadAdapter
import com.zeng.myworkout.viewmodel.WorkoutExerciseViewModel
import com.zeng.myworkout.viewmodel.getViewModel


class WorkoutExerciseViewHolder(
    private val context: Context,
    private val binding: ListItemWorkoutExerciseBinding
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var viewModel: WorkoutExerciseViewModel
    private lateinit var adapter: LoadAdapter

    init {
//            binding.numberSet.setOnClickListener {
//                var nbSet = binding.numberSet.text.toString().toInt()
//                showIntegerPicker(nbSet, "Number of sets:",
//                    // On value change
//                    { newValue ->
//                        nbSet = newValue
//                    },
//                    // On validation
//                    { exercise ->
//                        exercise.sets = nbSet
//                        viewModel.updateWorkoutExercise(exercise)
//                    }
//                )
//            }
//
//            binding.numberRep.setOnClickListener {
//                var nbRep = binding.numberRep.text.toString().toInt()
//                showIntegerPicker(nbRep, "Number of reps:",
//                    // On value change
//                    { newValue ->
//                        nbRep = newValue
//                    },
//                    // On validation
//                    { exercise ->
//                        exercise.reps = nbRep
//                        viewModel.updateWorkoutExercise(exercise)
//                    }
//                )
//            }
//
//            binding.weight.setOnClickListener {
//                onWeightClickListener()
//            }
    }

    fun bind(item: WorkoutExerciseDetail) {
        viewModel = getWorkoutExerciseViewModel(item.id!!)
        adapter = LoadAdapter(viewModel, item)

        binding.apply {
            exercise = item.detail
            executePendingBindings()
        }

        setupRecyclerView()
        subscribeUi()
    }

//    private fun showIntegerPicker(initialValue: Int,
//                                  message: String,
//                                  onValueChange: (Int) -> Unit,
//                                  onValidation: (WorkoutExercise) -> Unit) {
//        val pickerBinding = IntegerPickerBinding.inflate(inflater)
//
//        pickerBinding.picker.minValue = 1
//        pickerBinding.picker.maxValue = 100
//        pickerBinding.picker.value = initialValue
//        pickerBinding.picker.wrapSelectorWheel = true
//        pickerBinding.picker.setOnValueChangedListener { _, _, new -> onValueChange(new) }
//
//        val dialog = AlertDialog.Builder(context)
//            .setMessage(message)
//            .setPositiveButton("OK") { _, _ -> binding.exercise?.let(onValidation)}
//            .setNegativeButton("CANCEL") {  _, _ ->  }
//            .setView(pickerBinding.root)
//            .create()
//
//        dialog.window?.setDimAmount(0.1f)
//        dialog.show()
//    }
//
//    @SuppressLint("SetTextI18n")
//    fun weightToText(ex: WorkoutExercise): String {
//        return if (ex.weight.roundToInt().toFloat() == ex.weight) {
//            ex.weight.toInt().toString() + "kg"
//        } else {
//            DecimalFormat("#.##").format(ex.weight) + "kg"
//        }
//    }
//
//    // TODO PROBABLY NEED TO REFACTOR THIS
//    private fun onWeightClickListener() {
//        var weight = binding.exercise?.weight ?: 0f
//        val numberPickerBinding = DataBindingUtil.inflate<NumberPickerBinding>(inflater, R.layout.number_picker, null, false)
//        numberPickerBinding.weight = weight
//        numberPickerBinding.increase.setOnClickListener {
//            weight += 0.5f
//            if (weight > 1000f) {
//                weight = 1000f
//            }
//            numberPickerBinding.weight = weight
//        }
//        numberPickerBinding.decrease.setOnClickListener {
//            weight -= 0.5f
//            if (weight < 0f) {
//                weight = 0f
//            }
//            numberPickerBinding.weight = weight
//        }
//        numberPickerBinding.number.keyListener = DigitsKeyListener.getInstance(false, true)
//        numberPickerBinding.number.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if (s.isNullOrEmpty()) {
//                    weight = 0f
//                } else {
//                    if (s.startsWith(".")) {
//                        s.insert(0, "0")
//                    }
//                    weight = s.toString().toFloat()
//                    if (weight > 1000f) {
//                        weight = 1000f
//                    }
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })
//
//        val dialog = android.app.AlertDialog.Builder(context)
//            .setMessage("Weight")
//            .setPositiveButton("OK") { _, _ ->
//                binding.exercise?.let { exercise ->
//                    exercise.weight = weight
//                    viewModel.updateWorkoutExercise(exercise)
//                }
//            }
//            .setNegativeButton("CANCEL") {  _, _ ->  }
//            .setView(numberPickerBinding.root)
//            .create()
//
//        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
//        dialog.window?.setDimAmount(0.1f)
//        dialog.show()
//    }

    private fun setupRecyclerView() {
        binding.list.layoutManager = GridLayoutManager(context, context.resources.getInteger(R.integer.grid_load_row_count))
        binding.list.adapter = adapter
    }

    private fun subscribeUi() {
        viewModel.loads.observe(context as LifecycleOwner, Observer { loads ->
            adapter.submitList(loads)
        })
    }

    private fun getWorkoutExerciseViewModel(workoutExerciseId: Long): WorkoutExerciseViewModel {
        val workoutRepo = RepositoryUtils.getWorkoutRepository(context)
        return (context as AppCompatActivity).getViewModel({
            WorkoutExerciseViewModel(
                workoutRepo,
                workoutExerciseId
            )
        }, workoutExerciseId.toString())
    }
}
