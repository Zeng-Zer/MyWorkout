package com.zeng.myworkout2.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    private val viewModel by lazy {
        val routineRepository = RepositoryUtils.getRoutineRepository(this)
        val workoutRepository = RepositoryUtils.getWorkoutRepository(this)
        getViewModel({RoutineDetailViewModel(routineRepository, workoutRepository, routineId)})
    }

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityRoutineDetailBinding>(this, R.layout.activity_routine_detail)
    }

    private lateinit var adapter: RoutineDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        adapter = RoutineDetailAdapter(supportFragmentManager, lifecycle).also {
            binding.viewPager.adapter = it
        }

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, pos ->
            tab.text = "test $pos"
        }.attach()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            // TODO REMOVE
            viewModel.addWorkoutSql(WorkoutSql("Fullbody A", "test add workout", adapter.itemCount, routineId))
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
        }

        subscribeUi()
    }

    private fun subscribeUi() {
        // TODO SPLIT INTO SMALLER SQL ITEMS - IT SOLVES SORTING ISSUES | TRANSFORMATIONS.MAP ??
        viewModel.routine.observe(this, Observer { routine ->
            // Change toolbar title
            supportActionBar?.title = routine.name

            // Update workouts
            routine?.workouts?.apply {
                adapter.submitList(this.map { workout ->
                    WorkoutFragment(workout.id!!)
                })

                // Set title for each tabs
                this.mapIndexed { i, workout ->
                    binding.tabs.getTabAt(i)?.setText(workout.name)
                }
            }
        })
    }

}
