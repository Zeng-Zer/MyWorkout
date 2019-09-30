package com.zeng.myworkout2.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.zeng.myworkout2.R
import com.zeng.myworkout2.databinding.ActivityExerciseBinding
import com.zeng.myworkout2.util.RepositoryUtils
import com.zeng.myworkout2.view.adapter.ExerciseAdapter
import com.zeng.myworkout2.viewmodel.ExerciseViewModel
import com.zeng.myworkout2.viewmodel.getViewModel

class ExerciseActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityExerciseBinding>(this, R.layout.activity_exercise)
    }

    private val adapter by lazy {
        ExerciseAdapter()
    }

    private val viewModel by lazy {
        getViewModel({ExerciseViewModel(RepositoryUtils.getExerciseRepository(this))})
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Exercises"

        setupRecyclerView()
        setupFab()
        subscribeUi()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

        }
        return super.onOptionsItemSelected(item!!)
    }

    private fun setupRecyclerView() {
        binding.list.adapter = adapter
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {

        }
    }

    private fun subscribeUi() {
        viewModel.exercises.observe(this, Observer { exercises ->
            adapter.submitList(exercises)
        })
    }

    companion object {
        const val PICK_EXERCISE_REQUEST = 1
    }
}
