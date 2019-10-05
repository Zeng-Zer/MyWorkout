package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.FragmentHomeBinding
import com.zeng.myworkout.util.RepositoryUtils
import com.zeng.myworkout.viewmodel.HomeViewModel
import com.zeng.myworkout.viewmodel.getViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by lazy {
        getViewModel({HomeViewModel(
            RepositoryUtils.getRoutineRepository(requireContext()),
            RepositoryUtils.getWorkoutRepository(requireContext())
        )})
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        subscribeUi()
        setupButtons()
        return binding.root
    }

    private fun subscribeUi() {
        viewModel.user.observe(viewLifecycleOwner, Observer { it?.let { user ->
            binding.apply {
                if (user.workoutReferenceId != null) {
                    currentProgramLayout.visibility = View.VISIBLE
                    continueRoutine.visibility = View.VISIBLE
                } else {
                    currentProgramLayout.visibility = View.GONE
                    continueRoutine.visibility = View.GONE
                }
            }
        }})
    }

    private fun setupButtons() {
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)

        binding.apply {
            chooseRoutine.setOnClickListener {
                navController.navigate(R.id.navigation_routine)
            }
        }
    }
}