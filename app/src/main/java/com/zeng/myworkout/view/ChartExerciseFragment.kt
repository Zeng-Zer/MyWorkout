package com.zeng.myworkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zeng.myworkout.databinding.FragmentExerciseGraphBinding

class ChartExerciseFragment : Fragment() {

    private lateinit var binding: FragmentExerciseGraphBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExerciseGraphBinding.inflate(inflater, container, false)

        return binding.root
    }
}
