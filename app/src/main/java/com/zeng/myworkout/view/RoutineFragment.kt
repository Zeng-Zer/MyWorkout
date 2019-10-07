package com.zeng.myworkout.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zeng.myworkout.databinding.DialogRoutineFormBinding
import com.zeng.myworkout.databinding.FragmentRoutineBinding
import com.zeng.myworkout.model.Routine
import com.zeng.myworkout.util.RepositoryUtils
import com.zeng.myworkout.util.getViewModel
import com.zeng.myworkout.view.adapter.RoutineAdapter
import com.zeng.myworkout.viewmodel.RoutineViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class RoutineFragment : Fragment() {

    private lateinit var binding: FragmentRoutineBinding

    private val viewModel: RoutineViewModel by lazy {
        getViewModel({RoutineViewModel(
            RepositoryUtils.getRoutineRepository(requireContext())
        )})
    }

    private val adapter: RoutineAdapter by lazy {
        RoutineAdapter(viewModel, ::navRoutineDetailFragment, ::deleteRoutine)
    }

    private fun navRoutineDetailFragment(routineId: Long, isNew: Boolean) {
        val action = RoutineFragmentDirections.actionNavigationRoutineToNavigationRoutineDetail(isNew, routineId)
        findNavController().navigate(action)
    }

    private fun deleteRoutine(routine: Routine) {
        viewModel.deleteRoutine(routine)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoutineBinding.inflate(inflater, container, false)

        setupRecyclerView()
        subscribeUi()
        setupFab(inflater)

        return binding.root
    }

    private fun setupFab(inflater: LayoutInflater) {
        binding.fab.setOnClickListener{
            showNewRoutineDialog(inflater)
        }
    }

    private fun setupRecyclerView() {
        binding.list.adapter = adapter

        // prevent RecyclerView blinking on submitList
        val animator = binding.list.itemAnimator as SimpleItemAnimator
        animator.supportsChangeAnimations = false

        val helper = ItemTouchHelper(adapter.callback)
        helper.attachToRecyclerView(binding.list)
    }

    private fun subscribeUi() {
        viewModel.routines.observe(viewLifecycleOwner, Observer { routines ->
            adapter.submitList(routines)
        })
    }

    private fun showNewRoutineDialog(inflater: LayoutInflater) {
        val formDialog = DialogRoutineFormBinding.inflate(inflater, container, false)
        val dialog = AlertDialog.Builder(context)
            .setMessage("New routine")
            .setPositiveButton("CREATE") { _, _ ->
                val routine = Routine(
                    formDialog.name.text.toString(),
                    formDialog.description.text.toString()
                    // default order is 0
                )
                viewModel.viewModelScope.launch {
                    routine.id = viewModel.insertRoutineAsync(routine)
                    navRoutineDetailFragment(routine.id!!, true)
                }
            }
            .setNegativeButton("CANCEL") {  _, _ ->  }
            .setView(formDialog.root)
            .create()

        dialog.show()
    }

}