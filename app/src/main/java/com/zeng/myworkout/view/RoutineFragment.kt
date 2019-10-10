package com.zeng.myworkout.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import androidx.navigation.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.DialogRoutineFormBinding
import com.zeng.myworkout.databinding.FragmentRoutineBinding
import com.zeng.myworkout.model.Routine
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.util.DialogUtils
import com.zeng.myworkout.view.adapter.RoutineAdapter
import com.zeng.myworkout.viewmodel.RoutineViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RoutineFragment : Fragment() {

    private lateinit var binding: FragmentRoutineBinding
    private val navController by lazy { findNavController() }
    private val viewModel by viewModel<RoutineViewModel>()
    private val adapter: RoutineAdapter by lazy {
        RoutineAdapter(
            context = requireContext(),
            onClearView = { list -> viewModel.updateRoutine(list.map { it.routine }) },
            onItemClick = this::navRoutineDetailFragment,
            onMenuClick = this::showRoutineMenuPopup,
            onWorkoutShortcutClickNested = this::onWorkoutShortcutClickNested
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRoutineBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupFab(inflater)
        subscribeUi()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter.enableDrag()
        binding.list.adapter = adapter

        // prevent RecyclerView blinking on submitList
        val animator = binding.list.itemAnimator as SimpleItemAnimator
        animator.supportsChangeAnimations = false

        val helper = ItemTouchHelper(adapter.callback)
        helper.attachToRecyclerView(binding.list)
    }

    private fun setupFab(inflater: LayoutInflater) {
        binding.fab.setOnClickListener {
            showNewRoutineDialog(inflater)
        }
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
            .setNegativeButton("CANCEL") { _, _ -> }
            .setView(formDialog.root)
            .create()

        dialog.show()
    }

    private fun showRoutineMenuPopup(viewMenu: View, routine: Routine) {
        val popup = PopupMenu(requireContext(), viewMenu)
        popup.menuInflater.inflate(R.menu.routine_popup_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete_routine -> {
                    DialogUtils.openValidationDialog(
                        context = requireContext(),
                        message = "Delete ${routine.name} ?",
                        positiveFun = { viewModel.deleteRoutine(routine) }
                    )
                    true
                }
                // TODO Stats/History ?
                else -> false
            }
        }
        popup.show()
    }

    private fun onWorkoutShortcutClickNested(workout: Workout) {
        viewModel.viewModelScope.launch {
            val user = viewModel.getUser()
            // Check if the user currently has a session
            if (user?.workoutSessionId != null) {
                DialogUtils.openValidationDialog(
                    context = requireContext(),
                    message = "Close current item session ?",
                    positiveFun = {
                        val oldWorkoutId = user.workoutSessionId!!
                        viewModel.viewModelScope.launch {
                            viewModel.updateUserWorkout(workout)
                            viewModel.deleteWorkout(oldWorkoutId)
                            navigateToWorkout()
                        }
                    }
                )
            } else {
                viewModel.updateUserWorkout(workout)
                navigateToWorkout()
            }
        }
    }

    private fun navigateToWorkout() {
        val homeNav = navController.graph[R.id.home_nav] as NavGraph
        homeNav.startDestination = R.id.navigation_workout
        val action = RoutineFragmentDirections.actionGlobalToHomeNav()
        navController.navigate(action)
    }

    private fun navRoutineDetailFragment(routineId: Long, isNew: Boolean) {
        val action = RoutineFragmentDirections.actionNavigationRoutineToNavigationRoutineDetail(isNew, routineId)
        navController.navigate(action)
    }
}