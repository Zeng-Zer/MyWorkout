package com.zeng.myworkout2.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.zeng.myworkout2.R
import com.zeng.myworkout2.databinding.DialogRoutineFormBinding
import com.zeng.myworkout2.databinding.FragmentRoutineBinding
import com.zeng.myworkout2.model.Routine
import com.zeng.myworkout2.util.RepositoryUtils
import com.zeng.myworkout2.view.adapter.RoutineAdapter
import com.zeng.myworkout2.viewmodel.RoutineViewModel
import com.zeng.myworkout2.viewmodel.getViewModel
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
        RoutineAdapter(viewModel, ::showRoutineDetailActivity, ::deleteRoutineWithUndo)
    }

    private fun showRoutineDetailActivity(routineId: Long, isNew: Boolean) {
        val intent = Intent(activity, RoutineDetailActivity::class.java)
        intent.putExtra(resources.getString(R.string.intent_routine), routineId)
        intent.putExtra(resources.getString(R.string.intent_new_routine), isNew)
        activity?.startActivity(intent)
    }

    private fun deleteRoutineWithUndo(viewHolder: RoutineAdapter.RoutineViewHolder) {
        viewHolder.binding.routine?.let { routine ->
            // Save current list
            val oldList = adapter.currentList
            viewModel.deleteRoutineSql(routine)

            // Snackbar to restore old list
            val snackbar = Snackbar.make(binding.coordinator, "Routine deleted", Snackbar.LENGTH_LONG)
            snackbar.setAction("UNDO") {
                viewModel.upsertRoutineSql(oldList)
            }
            snackbar.show()
        }
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
                    emptyList(),
                    formDialog.name.text.toString(),
                    formDialog.description.text.toString()
                )
                viewModel.viewModelScope.launch {
                    routine.id = viewModel.insertRoutineSqlAsync(routine)
                    showRoutineDetailActivity(routine.id!!, true)
                }
            }
            .setNegativeButton("CANCEL") {  _, _ ->  }
            .setView(formDialog.root)
            .create()

        dialog.show()
    }

}