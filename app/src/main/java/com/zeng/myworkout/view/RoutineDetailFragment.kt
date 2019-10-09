package com.zeng.myworkout.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.DialogWorkoutFormBinding
import com.zeng.myworkout.databinding.FragmentRoutineDetailBinding
import com.zeng.myworkout.model.*
import com.zeng.myworkout.view.adapter.RoutineWorkoutAdapter
import com.zeng.myworkout.viewmodel.ExerciseViewModel
import com.zeng.myworkout.viewmodel.RoutineDetailViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RoutineDetailFragment : Fragment() {

    // Args
    private val args by navArgs<RoutineDetailFragmentArgs>()
    private val routineId by lazy { args.routineId }
    private val isNew by lazy { args.isNew }

    private lateinit var binding: FragmentRoutineDetailBinding
    private val navController by lazy { findNavController() }
    private val toolbar by lazy { (requireActivity() as AppCompatActivity).supportActionBar }
    private val viewModel by viewModel<RoutineDetailViewModel> { parametersOf(routineId) }

    private val sharedViewModel by sharedViewModel<ExerciseViewModel>()
    private val adapter by lazy { RoutineWorkoutAdapter(this) }
    private var onListChangeCallback: ((List<WorkoutName>) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRoutineDetailBinding.inflate(inflater, container, false)

        // Add new workout for new routine
        if (isNew) {
            addNewWorkout()
        }

        setHasOptionsMenu(true)
        setupViewPager()
        setupFab()
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
                val currentItem = adapter.currentList[binding.viewPager.currentItem]
                viewModel.deleteWorkoutById(currentItem.id)
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

    private fun setupViewPager() {
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 2
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

    private fun setupFab() {
        binding.fab.setOnClickListener {
            if (adapter.currentList.size >= binding.viewPager.currentItem) {
                // Disable fab to prevent double clicks
                binding.fab.isEnabled = false

                navController.navigate(R.id.action_navigation_routine_detail_to_navigation_exercise)
            }
        }
    }

    private fun subscribeUi() {
        // Change toolbar title
        viewModel.routine.observe(this, Observer { routine ->
            toolbar?.title = routine?.name
        })

        // Update workouts
        viewModel.workouts.observe(viewLifecycleOwner, Observer { it?.let { workouts ->
            if (workouts.isEmpty()) {
                binding.fab.hide()
            } else {
                binding.fab.show()
            }

            adapter.submitList(workouts) {
                onListChangeCallback?.invoke(workouts)

                workouts.forEachIndexed { i, workout ->
                    binding.tabs.getTabAt(i)?.text = workout.name
                }
            }
        }})

        // Add new exercises from ExerciseFragment
        sharedViewModel.exercisesToAdd.observe(viewLifecycleOwner, Observer { exercises ->
            if (!exercises.isNullOrEmpty()) {
                addExercises(
                    exercises.map { it.id!! },
                    adapter.currentList[binding.viewPager.currentItem].id
                )

                // TODO is there another way ?
                sharedViewModel.exercisesToAdd.value = null
            }
        })
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
                onListChangeCallback = { list ->
                    binding.viewPager.setCurrentItem(list.size, true)
                    onListChangeCallback = null
                }
            }
            .setNegativeButton("CANCEL") {  _, _ ->  }
            .setView(form.root)
            .create()

        dialog.show()
    }

    private fun addExercises(exerciseIds: List<Long>, workoutId: Long) {
        val exercisesWithLoads = exerciseIds.mapIndexed { i, exerciseId ->
            // Make a pair of exercise and their load list
            Pair(
                WorkoutExercise(
                    // Add element at the end with its order in the list of ids
                    i + adapter.itemCount,
                    workoutId,
                    exerciseId
                ),
                listOf(Load(LoadType.WEIGHT, 0F, 0, 0))
            )
        }
        viewModel.insertWorkoutExerciseWithLoads(exercisesWithLoads)
    }

}
