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
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.view.adapter.RoutineWorkoutAdapter
import com.zeng.myworkout.viewmodel.RoutineDetailViewModel
import kotlinx.android.synthetic.main.activity_main.*
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
    private val adapter by lazy { RoutineWorkoutAdapter(this) }
    private var onListChangeCallback: ((List<Workout>) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRoutineDetailBinding.inflate(inflater, container, false)

        // Add new workout for new routine
        if (isNew) {
            addNewWorkout()
        }

        setHasOptionsMenu(true)
        setupViewPager()
        setupFab()
        setupButton()
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
                if (adapter.itemCount > 0) {
                    val currentItem = adapter.currentList[binding.viewPager.currentItem]
                    viewModel.deleteWorkout(currentItem)
                }
            }

            // EDIT
            R.id.action_edit_workout -> {
                if (adapter.itemCount > 0) {
                    val currentItem = adapter.currentList[binding.viewPager.currentItem]
                    openEditForm(currentItem)
                }
            }

            R.id.action_move_right_workout -> {
                if (binding.viewPager.currentItem + 1 < adapter.itemCount) {
                    val from = binding.viewPager.currentItem
                    val to = from + 1
                    adapter.swapElements(from, to) {
                        binding.viewPager.setCurrentItem(to, false)
                        viewModel.updateWorkout(it)
                    }
                }
                return true
            }

            R.id.action_move_left_workout -> {
                if (binding.viewPager.currentItem - 1 >= 0) {
                    val from = binding.viewPager.currentItem
                    val to = from - 1
                    adapter.swapElements(from, to) { viewModel.updateWorkout(it) }
                    onListChangeCallback = {
                        binding.viewPager.setCurrentItem(to, false)
                        onListChangeCallback = null
                    }
                }
                return true
            }
        }
        return true
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 5
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

    private fun setupButton() {
        binding.button.setOnClickListener {
            addNewWorkout()
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
                binding.viewPager.visibility = View.GONE
                binding.button.visibility = View.VISIBLE
            } else {
                binding.fab.show()
                binding.viewPager.visibility = View.VISIBLE
                binding.button.visibility = View.GONE
            }

            adapter.submitList(workouts) {
                onListChangeCallback?.invoke(workouts)

                workouts.forEachIndexed { i, workout ->
                    binding.tabs.getTabAt(i)?.text = workout.name
                }
            }
        }})
    }

    private fun addNewWorkout() {
        val form = DialogWorkoutFormBinding.inflate(layoutInflater, null, false)
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage("New workout")
            .setPositiveButton("CREATE") { _, _ ->
                if (form.workoutName.text.isNotBlank()) {
                    // Add workout to the end
                    val workout = Workout(
                        form.workoutName.text.toString().trim(),
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
            }
            .setNegativeButton("CANCEL") {  _, _ ->  }
            .setView(form.root)
            .create()

        dialog.show()
    }

    private fun openEditForm(workout: Workout) {
        val formDialog = DialogWorkoutFormBinding.inflate(layoutInflater, container, false)
        formDialog.workoutName.setText(workout.name)
        val dialog = AlertDialog.Builder(context)
            .setMessage("Edit Routine")
            .setPositiveButton("EDIT") { _, _ ->
                workout.name = formDialog.workoutName.text.toString().trim()
                viewModel.updateWorkout(workout)
            }
            .setNegativeButton("CANCEL") { _, _ -> }
            .setView(formDialog.root)
            .create()

        dialog.show()
    }

}
