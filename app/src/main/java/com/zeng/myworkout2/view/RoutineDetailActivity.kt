package com.zeng.myworkout2.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.zeng.myworkout2.R
import com.zeng.myworkout2.databinding.ActivityRoutineDetailBinding
import com.zeng.myworkout2.util.RepositoryUtils
import com.zeng.myworkout2.view.adapter.RoutineDetailAdapter
import com.zeng.myworkout2.viewmodel.RoutineDetailViewModel
import com.zeng.myworkout2.viewmodel.getViewModel

class RoutineDetailActivity : AppCompatActivity() {

    private val viewModel by lazy {
        val routineRepository = RepositoryUtils.getRoutineRepository(this)
        val routineId = intent.extras?.getSerializable(resources.getString(R.string.intent_routine)) as Long
        getViewModel({RoutineDetailViewModel(routineRepository, routineId)})
    }

    private lateinit var adapter: RoutineDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityRoutineDetailBinding>(this, R.layout.activity_routine_detail)

        adapter = RoutineDetailAdapter(supportFragmentManager).also {
            binding.viewPager.adapter = it
        }
        binding.tabs.setupWithViewPager(binding.viewPager)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        viewModel.routine.observe(this, Observer { routine ->
            routine?.workouts?.apply {
                adapter.submitList(this.map { workout ->
                    WorkoutFragment(workout.id!!)
                })
            }
        })
    }

}
