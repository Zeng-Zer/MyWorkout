package com.zeng.myworkout2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.zeng.myworkout2.databinding.FragmentRoutineBinding
import com.zeng.myworkout2.util.InjectorUtils
import com.zeng.myworkout2.view.adapter.RoutineAdapter
import com.zeng.myworkout2.viewmodel.RoutineViewModel

class RoutineFragment : Fragment() {

    private lateinit var binding: FragmentRoutineBinding

    private val viewModel: RoutineViewModel by viewModels {
        InjectorUtils.provideRoutineViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoutineBinding.inflate(inflater, container, false)

        val adapter = RoutineAdapter()
//        adapter.disableSwipe()
        binding.list.adapter = adapter
//        val helper = ItemTouchHelper(adapter.callback)
//        helper.attachToRecyclerView(binding.list)

        subscribeUi(adapter)
        binding.fab.setOnClickListener{
            viewModel.addRoutine()
        }

        return binding.root
    }

    private fun subscribeUi(adapter: RoutineAdapter) {
        viewModel.routines.observe(viewLifecycleOwner, Observer { routines ->
            adapter.submitList(routines)
        })
    }

}