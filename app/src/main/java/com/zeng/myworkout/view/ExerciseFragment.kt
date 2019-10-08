package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.FragmentExerciseBinding
import com.zeng.myworkout.view.adapter.ExerciseAdapter
import com.zeng.myworkout.viewmodel.ExerciseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExerciseFragment : Fragment() {

    private lateinit var binding: FragmentExerciseBinding
    private val navController by lazy { findNavController() }
    private val viewModel by viewModel<ExerciseViewModel>()
    private val adapter by lazy { ExerciseAdapter(viewModel) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExerciseBinding.inflate(inflater, container, false)

        setupBackPressed()
        setupRecyclerView()
        setupFab()
        subscribeUi()
        return binding.root
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
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            // TODO FAB
        }
    }

    private fun subscribeUi() {
        viewModel.exercises.observe(this, Observer { exercises ->
            adapter.submitList(exercises)
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
}
