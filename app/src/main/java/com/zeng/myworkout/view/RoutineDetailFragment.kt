package com.zeng.myworkout.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.DialogWorkoutFormBinding
import com.zeng.myworkout.databinding.FragmentRoutineDetailBinding
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.util.RepositoryUtils
import com.zeng.myworkout.util.getViewModel
import com.zeng.myworkout.view.adapter.RoutineDetailAdapter
import com.zeng.myworkout.viewmodel.RoutineDetailViewModel
import com.zeng.myworkout.viewmodel.WorkoutViewModel

class RoutineDetailFragment : Fragment() {

    val args by navArgs<RoutineDetailFragmentArgs>()
    private val routineId by lazy { args.routineId }
    private val isNew by lazy { args.isNew }

    private lateinit var binding: FragmentRoutineDetailBinding

    private val viewModel by lazy {
        getViewModel({RoutineDetailViewModel(
            RepositoryUtils.getRoutineRepository(requireContext()),
            RepositoryUtils.getWorkoutRepository(requireContext()),
            routineId
        )})
    }

    private val adapter = RoutineDetailAdapter()

    private val workoutRecycledViewPool = RecyclerView.RecycledViewPool()
    private val workoutExerciseRecycledViewPool = RecyclerView.RecycledViewPool()

    private var onListChangeCallback: ((List<WorkoutItem>) -> Unit)? = null

    private fun currentItemIdx(): Int = binding.viewPager.currentItem
    private fun currentWorkoutItem(): WorkoutItem = adapter.currentList[currentItemIdx()]

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRoutineDetailBinding.inflate(inflater, container, false)

        // TODO
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Add new workout for new routine
        if (isNew) {
            addNewWorkout()
        }

        setupViewPager()
        setupFab()
        subscribeUi()
        return binding.root
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.routine_detail_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item?.itemId) {
//            android.R.id.home -> {
//                finish()
//                return true
//            }
//
//            R.id.action_new_workout -> {
//                addNewWorkout()
//                return true
//            }
//
//            R.id.action_delete_workout -> {
//                if (adapter.itemCount > 0) {
//                    // ACTUALLY THERE IS A BUG WHEN DELETING LAST VIEW
//                    // IT WILL BE FIXED IN THE NEXT MATERIAL COMPONENTS VERSION
//                    viewModel.deleteWorkoutById(currentWorkoutItem().workoutId)
//                }
//                return true
//            }
//
//            // TODO
////
////            R.id.action_move_right_workout -> {
////                if (viewPager.currentItemIdx + 1 < sectionsPagerAdapter.fragments.size) {
////                    sectionsPagerAdapter.swapTab(viewPager.currentItemIdx, viewPager.currentItemIdx + 1)
////                    viewPager.setCurrentItem(viewPager.currentItemIdx + 1, true)
////                }
////                return true
////            }
////
////            R.id.action_move_left_workout -> {
////                if (viewPager.currentItemIdx - 1 >= 0) {
////                    sectionsPagerAdapter.swapTab(viewPager.currentItemIdx, viewPager.currentItemIdx - 1)
////                    viewPager.setCurrentItem(viewPager.currentItemIdx - 1, true)
////                }
////                return true
////            }
//        }
//        return super.onOptionsItemSelected(item!!)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ExerciseActivity.PICK_EXERCISE_REQUEST) {
            binding.fab.isEnabled = true
            if (resultCode == Activity.RESULT_OK) {
                @Suppress("UNCHECKED_CAST")
                val exerciseIds = data?.extras?.getSerializable(resources.getString(R.string.intent_result_list)) as Array<Long>? ?: emptyArray()
                currentWorkoutItem().addExercises(exerciseIds)
                // Enable fab after getting exercise activity results
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            if (adapter.currentList.size >= binding.viewPager.currentItem) {
                // Disable fab to prevent double clicks
                binding.fab.isEnabled = false

                // Start activity to get Exercises
                // TODO
//                val intent = Intent(this, ExerciseActivity::class.java)
//                startActivityForResult(intent, ExerciseActivity.PICK_EXERCISE_REQUEST)
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
            // TODO
//            supportActionBar?.title = routine?.name
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
        val form = DataBindingUtil.inflate<DialogWorkoutFormBinding>(layoutInflater, R.layout.dialog_workout_form, null, false)
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
