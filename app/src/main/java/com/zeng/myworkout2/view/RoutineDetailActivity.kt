package com.zeng.myworkout2.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.google.android.material.tabs.TabLayoutMediator
import com.zeng.myworkout2.R
import com.zeng.myworkout2.databinding.ActivityRoutineDetailBinding
import com.zeng.myworkout2.model.WorkoutSql
import com.zeng.myworkout2.util.RepositoryUtils
import com.zeng.myworkout2.view.adapter.RoutineDetailAdapter
import com.zeng.myworkout2.viewmodel.RoutineDetailViewModel
import com.zeng.myworkout2.viewmodel.getViewModel

class RoutineDetailActivity : AppCompatActivity() {

    private val routineId by lazy { intent.extras?.getLong(resources.getString(R.string.intent_routine)) as Long }

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityRoutineDetailBinding>(this, R.layout.activity_routine_detail)
    }

    private val viewModel by lazy {
        getViewModel({RoutineDetailViewModel(
            RepositoryUtils.getRoutineRepository(this),
            RepositoryUtils.getWorkoutRepository(this),
            routineId
        )})
    }

    private val adapter: RoutineDetailAdapter by lazy {
        RoutineDetailAdapter(supportFragmentManager, lifecycle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViewPager()
        setupFab()
        subscribeUi()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.routine_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_new_workout -> {
                viewModel.addWorkoutSql(WorkoutSql("Fullbody A", "test add workout", adapter.itemCount, routineId))
                return true
            }

            // TODO
//
//            R.id.action_delete_workout -> {
//                sectionsPagerAdapter.removeTab(viewPager.currentItem)
//                return true
//            }
//
//            R.id.action_move_right_workout -> {
//                if (viewPager.currentItem + 1 < sectionsPagerAdapter.fragments.size) {
//                    sectionsPagerAdapter.swapTab(viewPager.currentItem, viewPager.currentItem + 1)
//                    viewPager.setCurrentItem(viewPager.currentItem + 1, true)
//                }
//                return true
//            }
//
//            R.id.action_move_left_workout -> {
//                if (viewPager.currentItem - 1 >= 0) {
//                    sectionsPagerAdapter.swapTab(viewPager.currentItem, viewPager.currentItem - 1)
//                    viewPager.setCurrentItem(viewPager.currentItem - 1, true)
//                }
//                return true
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupFab() {
        val fab = binding.fab

        fab.setOnClickListener {
            adapter.workouts[binding.viewPager.currentItem].addExercise()
        }

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                when(state) {
                    SCROLL_STATE_IDLE -> fab.show()
                    else -> fab.hide()
                }
            }
        })
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewPager){ _, _ -> }.attach()
    }

    private fun subscribeUi() {
        // Change toolbar title
        viewModel.routine.observe(this, Observer { routine ->
            supportActionBar?.title = routine.name
        })

        // Update workouts
        viewModel.workouts.observe(this, Observer {
            it?.let { workouts ->
                adapter.submitList(workouts.map { workout ->
                    WorkoutFragment(workout.id!!)
                })

                // Set title for each tabs
                workouts.mapIndexed { i, workout ->
                    binding.tabs.getTabAt(i)?.setText(workout.name)
                }
            }
        })
    }

}
