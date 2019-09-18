package com.zeng.myworkout2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.zeng.myworkout2.databinding.FragmentWorkoutBinding
import com.zeng.myworkout2.util.RepositoryUtils
import com.zeng.myworkout2.viewmodel.WorkoutViewModel
import com.zeng.myworkout2.viewmodel.getViewModel

class WorkoutFragment(val workoutId: Long) : Fragment() {

    private lateinit var binding: FragmentWorkoutBinding

    // Set view model with its workout
    val viewModel : WorkoutViewModel by lazy {
        val workoutRepository = RepositoryUtils.getWorkoutRepository(requireContext())
        getViewModel({ WorkoutViewModel(workoutRepository, workoutId)}, workoutId.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        viewModel.workout.observe(viewLifecycleOwner, Observer {
            it?.apply {
                binding.textView.text = this.name
            }
        })

        return binding.root
    }
}
