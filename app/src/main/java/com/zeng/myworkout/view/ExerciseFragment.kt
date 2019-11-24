package com.zeng.myworkout.view

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.DialogExerciseFormBinding
import com.zeng.myworkout.databinding.FragmentExerciseBinding
import com.zeng.myworkout.model.Exercise
import com.zeng.myworkout.util.DialogUtils
import com.zeng.myworkout.util.minusAssign
import com.zeng.myworkout.util.plusAssign
import com.zeng.myworkout.view.adapter.ExerciseAdapter
import com.zeng.myworkout.viewmodel.ExerciseViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ExerciseFragment : Fragment() {

    private lateinit var binding: FragmentExerciseBinding
    private val navController by lazy { findNavController() }
    private val viewModel by sharedViewModel<ExerciseViewModel>()
    private val adapter by lazy {
        ExerciseAdapter(
            this::onCheckedItem,
            this::onSwipedItem
        )
    }
    private var exercises: List<Exercise> = emptyList()
    private var categoryFilter: String = "All"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExerciseBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        setupBackPressed()
        setupRecyclerView()
        setupFilters()
        subscribeUi()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.exercise_page_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
            }

            R.id.new_exercise -> {
                newExerciseDialog()
            }
        }
        return true
    }

    private fun newExerciseDialog() {
        viewModel.viewModelScope.launch {
            val categories = viewModel.getCategories().map { it.id }

            val exerciseForm = DialogExerciseFormBinding.inflate(layoutInflater, null, false)
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories.toTypedArray())
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            exerciseForm.spinner.adapter = adapter

            val dialog = AlertDialog.Builder(requireContext())
                .setMessage("New Exercise")
                .setPositiveButton("Create") { _, _ ->
                    if (!exerciseForm.name.text.isNullOrBlank()) {
                        val exercise = Exercise(exerciseForm.name.text.toString(), exerciseForm.spinner.selectedItem as String)
                        viewModel.insertExercise(exercise)
                    }
                }
                .setNegativeButton("Cancel") {  _, _ ->  }
                .setView(exerciseForm.root)
                .create()

            dialog.show()
        }
    }

    private fun setupBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.exercisesToAdd.value = null
            navController.navigateUp()
        }
    }

    private fun setupRecyclerView() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)
        binding.list.adapter = adapter

        adapter.enableSwipe()
        val helper = ItemTouchHelper(adapter.callback)
        helper.attachToRecyclerView(binding.list)
    }

    private fun setupFilters() {
        viewModel.viewModelScope.launch {
            // Init spinner
            val categories = viewModel.getCategories().map { it.id }.toMutableList()
            categories.add(0, "All")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories.toTypedArray())
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                    categoryFilter = parent?.getItemAtPosition(pos) as String? ?: "All"
                    submitFilteredList()
                }

            }
        }
    }

    private fun submitFilteredList() {
        val filtered = exercises.filter { categoryFilter == "All" || it.category == categoryFilter }
        adapter.submitList(filtered)
    }

    private fun subscribeUi() {
        viewModel.exercises.observe(this, Observer { exercises ->
            this.exercises = exercises
            submitFilteredList()
        })

        // Change toolbar icon
        viewModel.hasChecked.observe(this, Observer { hasChecked ->
            // TODO ANIMATE
            if (hasChecked) {
                (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_check_white_24dp))
            } else {
                (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_white_24dp))
            }
        })
    }

    private fun onCheckedItem(checked: Boolean, item: Exercise) {
        if (checked) {
            viewModel.exercisesToAdd += item
        } else {
            viewModel.exercisesToAdd -= item
        }
    }

    private fun onSwipedItem(item: Exercise) {
        DialogUtils.openValidationDialog(
            context = requireContext(),
            message = "Remove exercise " + item.name + " ?",
            positiveFun = { viewModel.deleteExercise(item) },
            negativeFun = { adapter.notifyDataSetChanged() }
        )
    }
}
