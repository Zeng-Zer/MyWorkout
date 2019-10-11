package com.zeng.myworkout.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import androidx.navigation.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.FragmentWorkoutBinding
import com.zeng.myworkout.databinding.NumberPickerBinding
import com.zeng.myworkout.model.Load
import com.zeng.myworkout.model.LoadType
import com.zeng.myworkout.model.WorkoutExerciseDetail
import com.zeng.myworkout.util.DialogUtils
import com.zeng.myworkout.util.weightToString
import com.zeng.myworkout.view.adapter.WorkoutExerciseAdapter
import com.zeng.myworkout.viewmodel.HomeViewModel
import com.zeng.myworkout.viewmodel.WorkoutViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WorkoutFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutBinding
    private val homeViewModel by viewModel<HomeViewModel>()
    private val workoutViewModel by viewModel<WorkoutViewModel> { parametersOf(this) }
    private val recycledViewPool by lazy { RecyclerView.RecycledViewPool() }
    private val adapter by lazy { WorkoutExerciseAdapter(
        context = requireContext(),
        recycledViewPool = recycledViewPool,
        session = true,
        onClearView = { list -> workoutViewModel.updateAllWorkoutExercise(list.map{ it.exercise }) },
        onMenuClick = this::showWorkoutExerciseMenuPopup,
        onLoadClickNested = this::setButtonSessionReps,
        onLoadTextClickNested = this::setTextEditLoad
    )}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).supportActionBar?.title = ""

        setupButtons()
        setupRecyclerView()
        subscribeUi()
        return binding.root
    }

    private fun setupButtons() {
        binding.apply {
            cancel.setOnClickListener {
                homeViewModel.viewModelScope.launch {
                    homeViewModel.deleteWorkoutSession()
                    navigateToHome()
                }
            }
            finish.setOnClickListener {
                homeViewModel.viewModelScope.launch {
                    homeViewModel.finishCurrentWorkoutSession()
                    navigateToHome()
                }
            }
        }
    }

    private fun navigateToHome() {
        val homeNav = findNavController().graph[R.id.home_nav] as NavGraph
        homeNav.startDestination = R.id.navigation_home
        findNavController().navigate(R.id.action_navigation_workout_to_navigation_home)
    }

    private fun setupRecyclerView() {
        binding.apply {
            adapter.viewLifecycleOwner = viewLifecycleOwner
            adapter.enableDrag()
            list.adapter = adapter

            // prevent RecyclerView blinking on submitList
            val animator = list.itemAnimator as SimpleItemAnimator
            animator.supportsChangeAnimations = false

            val helper = ItemTouchHelper(adapter.callback)
            helper.attachToRecyclerView(list)
        }
    }

    private fun subscribeUi() {
        homeViewModel.workoutSession.observe(viewLifecycleOwner, Observer { workout ->
            (requireActivity() as MainActivity).supportActionBar?.title = workout?.name
            workoutViewModel.workoutId.value = workout?.id
        })

        workoutViewModel.exercises.observe(viewLifecycleOwner, Observer { exercises ->
            adapter.submitList(exercises)
        })
    }
    private fun showWorkoutExerciseMenuPopup(menuView: View, item: WorkoutExerciseDetail, loadCount: Int) {
        val popup = PopupMenu(requireContext(), menuView)
        popup.menuInflater.inflate(R.menu.workout_exercise_popup_menu, popup.menu)
        if (loadCount <= 1) {
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
                    workoutViewModel.insertLoad(newLoad)
                    true
                }
                R.id.remove_set -> {
                    if (loads.size > 1) {
                        workoutViewModel.deleteLoad(loads.last())
                    }
                    true
                }
                R.id.remove_exercise -> {
                    DialogUtils.openValidationDialog(
                        context = requireContext(),
                        message = "Remove ${detail.name} ?",
                        positiveFun = { workoutViewModel.deleteWorkoutExercise(exercise) }
                    )
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    // TODO SET OTHER TYPE
    private fun setButtonSessionReps(view: View, load: Load) {
        load.repsDone -= 1
        if (load.repsDone < -1) {
            load.repsDone = load.reps
        }

        load.setRepsButtonText(view as Button, true, resources)
        workoutViewModel.updateLoad(load)
    }

    // TODO REFACTOR THIS
    private fun setTextEditLoad(view: View, load: Load) {
        val editText = view as EditText
        var weight = load.value
        val numberPickerBinding = NumberPickerBinding.inflate(layoutInflater, null, false)
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
                workoutViewModel.updateLoad(load)
            }
            .setNegativeButton("CANCEL") {  _, _ ->  }
            .setView(numberPickerBinding.root)
            .create()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.window?.setDimAmount(0.1f)
        dialog.show()
    }
}