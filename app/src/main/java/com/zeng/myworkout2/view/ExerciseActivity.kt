package com.zeng.myworkout2.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
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
                finishWithResult()
                return true
            }

        }
        return super.onOptionsItemSelected(item!!)
    }

    private fun finishWithResult() {
        val exercises = adapter.checkedList.map { exercise ->
            exercise.id
        }.toTypedArray()

        val intent = Intent()
        intent.putExtra(resources.getString(R.string.intent_result_list), exercises)

        setResult(Activity.RESULT_OK, intent)

        finish()
    }

    private fun setupRecyclerView() {
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)
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

        // TODO ANIMATION WITH TOOLBAR
        // Change toolbar icon
        adapter.hasChecked.observe(this, Observer { hasChecked ->
            if (hasChecked) {
                supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_check_white_24dp))
            } else {
                supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_white_24dp))
            }
        })
    }

    companion object {
        const val PICK_EXERCISE_REQUEST = 1
    }
}
