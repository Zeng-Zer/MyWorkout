package com.zeng.myworkout.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.DialogWorkoutFormBinding
import com.zeng.myworkout.databinding.FragmentRoutineDetailBinding
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.util.RepositoryUtils
import com.zeng.myworkout.util.getSharedViewModel
import com.zeng.myworkout.util.getViewModel
import com.zeng.myworkout.view.adapter.RoutineDetailAdapter
import com.zeng.myworkout.viewmodel.ExerciseViewModel
import com.zeng.myworkout.viewmodel.RoutineDetailViewModel
import com.zeng.myworkout.viewmodel.WorkoutViewModel

class RoutineDetailFragment : Fragment() {

    private val args by navArgs<RoutineDetailFragmentArgs>()
    private val routineId by lazy { args.routineId }
    private val isNew by lazy { args.isNew }

    private lateinit var binding: FragmentRoutineDetailBinding
    private val navController by lazy { findNavController() }
    private val toolbar by lazy {
        (requireActivity() as AppCompatActivity).supportActionBar
    }

    private val viewModel by lazy {
        getViewModel({RoutineDetailViewModel(
            RepositoryUtils.getRoutineRepository(requireContext()),
            RepositoryUtils.getWorkoutRepository(requireContext()),
            routineId
        )})
    }

    private val exerciseVm by lazy {
        getSharedViewModel({ExerciseViewModel(
            RepositoryUtils.getExerciseRepository(requireContext())
        )})
    }

    private val adapter = RoutineDetailAdapter()

    private val workoutRecycledViewPool = RecyclerView.RecycledViewPool()
    private val workoutExerciseRecycledViewPool = RecyclerView.RecycledViewPool()

    private var onListChangeCallback: ((List<WorkoutItem>) -> Unit)? = null

    private fun currentItemIdx(): Int = binding.viewPager.currentItem
    private fun currentWorkoutItem(): WorkoutItem? = adapter.currentList.getOrNull(currentItemIdx())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRoutineDetailBinding.inflate(inflater, container, false)

        // Add new workout for new routine
        if (isNew) {
            addNewWorkout()
        }

        setHasOptionsMenu(true)
        setupFab()
        setupViewPager()
        subscribeUi()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.routine_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
            }
            R.id.action_new_workout -> {
                addNewWorkout()
            }

            R.id.action_delete_workout -> {
                // ACTUALLY THERE IS A BUG WHEN DELETING LAST VIEW
                // IT WILL BE FIXED IN THE NEXT MATERIAL COMPONENTS VERSION
                currentWorkoutItem()?.workoutId?.let { viewModel.deleteWorkoutById(it) }
            }

            // TODO
//
//            R.id.action_move_right_workout -> {
//                if (viewPager.currentItemIdx + 1 < sectionsPagerAdapter.fragments.size) {
//                    sectionsPagerAdapter.swapTab(viewPager.currentItemIdx, viewPager.currentItemIdx + 1)
//                    viewPager.setCurrentItem(viewPager.currentItemIdx + 1, true)
//                }
//                return true
//            }
//
//            R.id.action_move_left_workout -> {
//                if (viewPager.currentItemIdx - 1 >= 0) {
//                    sectionsPagerAdapter.swapTab(viewPager.currentItemIdx, viewPager.currentItemIdx - 1)
//                    viewPager.setCurrentItem(viewPager.currentItemIdx - 1, true)
//                }
//                return true
        }
        return true
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            if (adapter.currentList.size >= binding.viewPager.currentItem) {
                // Disable fab to prevent double clicks
                binding.fab.isEnabled = false

                navController.navigate(R.id.action_navigation_routine_detail_to_navigation_exercise)
            }
        }
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 7
        TabLayoutMediator(binding.tabs, binding.viewPager){ _, _ -> }.attach()

        // Hide fab when changing fragment
        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                if (adapter.itemCount > 0) {
                    when (state) {
                        ViewPager2.SCROLL_STATE_IDLE -> binding.fab.show()
                        else -> binding.fab.hide()
                    }
                }
            }
        })
    }

    private fun subscribeUi() {
        // Change toolbar title
        viewModel.routine.observe(this, Observer { routine ->
            toolbar?.title = routine?.name
        })

        // Update workouts
        viewModel.workouts.observe(this, Observer {
            it?.let { workouts ->
                if (workouts.isEmpty()) {
                    binding.fab.hide()
                } else {
                    binding.fab.show()
                }

                val newWorkoutItems = workouts.map { workout ->
                    createWorkoutItem(workout.id!!)
                }

                adapter.submitList(newWorkoutItems) {
                    // On list committed
                    onListChangeCallback?.invoke(newWorkoutItems)

                    // Set title for each tabs
                    workouts.forEachIndexed { i, workout ->
                        binding.tabs.getTabAt(i)?.text = workout.name
                    }
                }
            }
        })

        // Add new exercises from ExerciseFragment
        exerciseVm.exercisesToAdd.observe(viewLifecycleOwner, Observer { exercises ->
            if (!exercises.isNullOrEmpty()) {
                currentWorkoutItem()?.addExercises(exercises.map { it.id!! })
                // TODO is there another way ?
                exerciseVm.exercisesToAdd.value = null
            }
        })
    }

    private fun createWorkoutItem(workoutId: Long): WorkoutItem {
        val workoutRepo = RepositoryUtils.getWorkoutRepository(requireContext())
        val workoutViewModel = getViewModel({WorkoutViewModel(
            workoutRepo,
            workoutId
        )}, workoutId.toString())
        return WorkoutItem(this, workoutRecycledViewPool, workoutExerciseRecycledViewPool, workoutId, workoutViewModel)
    }

    private fun addNewWorkout() {
        val form = DialogWorkoutFormBinding.inflate(layoutInflater, null, false)
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage("New workout")
            .setPositiveButton("CREATE") { _, _ ->
                // Add workout to the end
                val workout = Workout(
                    form.workoutName.text.toString(),
                    "",
                    adapter.itemCount,
                    routineId,
                    true
                )

                viewModel.insertWorkout(workout)

                // Focus newly added view
                onListChangeCallback = { workoutItems ->
                    binding.viewPager.setCurrentItem(workoutItems.size, true)
                    onListChangeCallback = null
                }
            }
            .setNegativeButton("CANCEL") {  _, _ ->  }
            .setView(form.root)
            .create()

        dialog.show()
    }

}
