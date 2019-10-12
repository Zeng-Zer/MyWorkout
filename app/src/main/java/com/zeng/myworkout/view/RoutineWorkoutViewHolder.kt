package com.zeng.myworkout.view

import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.FragmentRoutineWorkoutBinding
import com.zeng.myworkout.databinding.IntegerPickerBinding
import com.zeng.myworkout.databinding.NumberPickerBinding
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.LoadType
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.util.DialogUtils
import com.zeng.myworkout.util.weightToString
import com.zeng.myworkout.view.adapter.WorkoutExerciseAdapter
import com.zeng.myworkout.viewmodel.WorkoutViewModel
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class RoutineWorkoutViewHolder(
    private val binding: FragmentRoutineWorkoutBinding,
    private val fragment: Fragment,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val loadRecycledViewPool: RecyclerView.RecycledViewPool
) : RecyclerView.ViewHolder(binding.root) {
    private val context = fragment.requireContext()
    private val viewLifecycleOwner = fragment.viewLifecycleOwner
    private lateinit var viewModel: WorkoutViewModel
    private val adapter by lazy { WorkoutExerciseAdapter(
        context = context,
        recycledViewPool = loadRecycledViewPool,
        session = false,
        onClearView = { list -> viewModel.updateAllWorkoutExercise(list.map{ it.exercise }) },
        onMenuClick = this::showWorkoutExerciseMenuPopup,
        onLoadClickNested = this::setButtonEdit,
        onLoadTextClickNested = this::setTextEditLoad
    )}

    fun bind(item: Workout) {
        viewModel = fragment.get(named("factory")) { parametersOf(fragment, item.id) }
        viewModel.workoutId.value = item.id

        setupRecyclerView()
        subscribeUi()
    }

    private fun setupRecyclerView() {
        adapter.enableDrag()
        adapter.viewLifecycleOwner = fragment.viewLifecycleOwner
        binding.list.adapter = adapter
        binding.list.setRecycledViewPool(recycledViewPool)

        // prevent RecyclerView blinking on submitList
        val animator = binding.list.itemAnimator as SimpleItemAnimator
        animator.supportsChangeAnimations = false

        val helper = ItemTouchHelper(adapter.callback)
        helper.attachToRecyclerView(binding.list)
    }

    private fun subscribeUi() {
        viewModel.exercises.observe(viewLifecycleOwner, Observer { exercises ->
            adapter.submitList(exercises)
        })
    }

    private fun showWorkoutExerciseMenuPopup(menuView: View, item: WorkoutExerciseDetail, loadSize: Int) {
        val popup = PopupMenu(context, menuView)
        popup.menuInflater.inflate(R.menu.workout_exercise_popup_menu, popup.menu)
        if (loadSize <= 1) {
            popup.menu.removeItem(R.id.remove_set)
        }
        popup.setOnMenuItemClickListener { menuItem ->
            val exercise = item.exercise
            val loads = item.loads
            val detail = item.detail
            when (menuItem.itemId) {
                R.id.add_set -> {
                    val last = loads.lastOrNull() ?: Load(LoadType.WEIGHT, 0F, 0, 0, exercise.id)
                    val newLoad = last.copy(id = null, order = last.order + 1)
                    viewModel.insertLoad(newLoad)
                    true
                }
                R.id.remove_set -> {
                    if (loads.size > 1) {
                        viewModel.deleteLoad(loads.last())
                    }
                    true
                }
                R.id.remove_exercise -> {
                    DialogUtils.openValidationDialog(
                        context = context,
                        message = "Remove ${detail.name} ?",
                        positiveFun = { viewModel.deleteWorkoutExercise(exercise) }
                    )
                    true
                }
                else -> false
            }
        }
        popup.show()
    }


    private fun setButtonEdit(view: View, load: Load) {
        val button = view as Button
        val pickerBinding = IntegerPickerBinding.inflate(fragment.layoutInflater)

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
                load.setRepsButtonText(button, false, fragment.resources)
                viewModel.updateLoad(load)
            }
            .setNegativeButton("CANCEL") {  _, _ ->  }
            .setView(pickerBinding.root)
            .create()

        dialog.window?.setDimAmount(0.1f)
        dialog.show()
    }


    // TODO REFACTOR THIS
    private fun setTextEditLoad(view: View, load: Load) {
        val editText = view as EditText
        var weight = load.value
        val numberPickerBinding = NumberPickerBinding.inflate(fragment.layoutInflater, null, false)
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
                editText.setText(weight.weightToString())
                viewModel.updateLoad(load)
            }
            .setNegativeButton("CANCEL") {  _, _ ->  }
            .setView(numberPickerBinding.root)
            .create()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.window?.setDimAmount(0.1f)
        dialog.show()
    }
}