package com.zeng.myworkout.logic

import android.content.Context
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.IntegerPickerBinding
import com.zeng.myworkout.databinding.NumberPickerBinding
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.util.weightToString
import com.zeng.myworkout.viewmodel.WorkoutViewModel

// Helper function to set button value based on the load
fun setLoadButton(load: Load, button: Button, session: Boolean, resources: Resources) {
    with(load) {
        if (session) {
            if (repsDone == -1) {
                button.text = reps.toString()
                button.background = resources.getDrawable(R.drawable.my_button_unselected)
            } else {
                button.text = repsDone.toString()
                button.background = resources.getDrawable(R.drawable.my_button_ripple)
            }
        } else {
            button.text = reps.toString()
        }
    }
}

// Editable button when browsing routine workouts
fun setButtonEdit(context: Context, viewModel: WorkoutViewModel): (View, Load) -> Unit {
    return { view: View, load: Load ->
        val button = view as Button
        val pickerBinding = IntegerPickerBinding.inflate(LayoutInflater.from(context))

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
                setLoadButton(load, button, false, context.resources)
                viewModel.updateLoad(load)
            }
            .setNegativeButton("CANCEL") { _, _ -> }
            .setView(pickerBinding.root)
            .create()

        dialog.window?.setDimAmount(0.1f)
        dialog.show()
    }
}

// TODO SET OTHER TYPE
fun setButtonSessionReps(context: Context, viewModel: WorkoutViewModel): (View, Load) -> Unit {
    return { view: View, load: Load ->
        load.repsDone -= 1
        if (load.repsDone < -1) {
            load.repsDone = load.reps
        }

        setLoadButton(load, view as Button, true, context.resources)
        viewModel.updateLoad(load)
    }
}

// TODO REFACTOR THIS
// Editable text when clicking on load value
fun setTextEditLoad(context: Context, viewModel: WorkoutViewModel): (View, Load) -> Unit {
    return { view: View, load: Load ->
        val editText = view as EditText
        var weight = load.value
        val numberPickerBinding =
            NumberPickerBinding.inflate(LayoutInflater.from(context), null, false)
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
            .setNegativeButton("CANCEL") { _, _ -> }
            .setView(numberPickerBinding.root)
            .create()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.window?.setDimAmount(0.1f)
        dialog.show()
    }
}
