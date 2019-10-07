package com.zeng.myworkout.view

import android.os.Bundle
import android.util.Log
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

    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    private lateinit var binding: ActivityMainBinding
    private var navBarShown = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_workout,
                R.id.navigation_routine,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        setupNavControllerListener()
    }

    private fun setupNavControllerListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_routine -> {
                    Log.d("TEST", "Routine")
                }
                R.id.navigation_home -> {
                    Log.d("TEST", "Home")
                }
                R.id.navigation_workout -> {
                    Log.d("TEST", "Workout")
                }
                R.id.navigation_notifications -> {
                    Log.d("TEST", "Notifications")
                    hideBottomNavigationView(binding.navView)
                }
            }

            if (destination.id != R.id.navigation_notifications && !navBarShown) {
                showBottomNavigationView(binding.navView)
            }
        }
    }

    private fun hideBottomNavigationView(bottomView: BottomNavigationView) {
        if (navBarShown) {
            bottomView.clearAnimation()
            val animator = bottomView.animate()
            animator.translationY(bottomView.height.toFloat()).duration = 300
            navBarShown = false
        }
    }

    private fun showBottomNavigationView(bottomView: BottomNavigationView) {
        if (!navBarShown) {
            bottomView.clearAnimation()
            val animator = bottomView.animate()
            animator.translationY(0F).duration = 300
            navBarShown = true
        }
    }
}
