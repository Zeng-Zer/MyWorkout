package com.zeng.myworkout2.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.zeng.myworkout2.R
import com.zeng.myworkout2.databinding.ActivityRoutineDetailBinding
import com.zeng.myworkout2.util.InjectorUtils
import com.zeng.myworkout2.viewmodel.RoutineDetailViewModel

class RoutineDetailActivity : AppCompatActivity() {

    private val viewModel: RoutineDetailViewModel by viewModels {
        InjectorUtils.provideRoutineDetailViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityRoutineDetailBinding>(this, R.layout.activity_routine_detail)

        val routineId = intent.extras?.getSerializable(resources.getString(R.string.intent_routine)) as Long
        viewModel.setRoutine(routineId)
        viewModel.routine.observe(this, Observer {
            it?.workouts
        })
//        binding.viewPager.adapter = RoutineDetailAdapter(supportFragmentManager)
        binding.handler = this
        binding.manager = supportFragmentManager

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(binding.viewPager)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

//    @BindingAdapter("bind:handler")
//    open fun bindViewPagerAdapter(view: ViewPager, activity: RoutineDetailActivity) {
////        view.adapter = RoutineDetailAdapter()
//    }
}
