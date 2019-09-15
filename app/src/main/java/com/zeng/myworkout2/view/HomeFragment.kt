package com.zeng.myworkout2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.zeng.myworkout2.R
import com.zeng.myworkout2.util.InjectorUtils
import com.zeng.myworkout2.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels {
        InjectorUtils.provideHomeViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        root.setOnClickListener{
            homeViewModel.viewModelScope.launch {
                homeViewModel.changeWorkout(8)
            }
        }
        homeViewModel.workout.observe(this, Observer {
            it?.workout?.apply {
                textView.text = name
            }
        })
        return root
    }
}