package com.zeng.myworkout.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var navBarShown = true
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupNavController()
        setupNavControllerListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun setupNavController() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_workout,
                R.id.navigation_routine,
                R.id.navigation_history
            )
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun setupNavControllerListener() {
        val hideBottomViewIds = listOf(
            R.id.navigation_routine_detail,
            R.id.navigation_exercise
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (hideBottomViewIds.contains(destination.id)) {
                hideBottomNavigationView(binding.navView)
            } else {
                showBottomNavigationView(binding.navView)
            }
        }
    }

    private fun hideBottomNavigationView(bottomView: BottomNavigationView) {
        if (navBarShown) {
            bottomView.visibility = View.GONE
            navBarShown = false
        }
    }

    private fun showBottomNavigationView(bottomView: BottomNavigationView) {
        if (!navBarShown) {
            bottomView.visibility = View.VISIBLE
            navBarShown = true
        }
    }
}
