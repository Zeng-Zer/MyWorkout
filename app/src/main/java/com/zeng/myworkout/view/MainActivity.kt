package com.zeng.myworkout.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zeng.myworkout.R
import com.zeng.myworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }
    val appBarConfiguration by lazy { AppBarConfiguration(
        setOf(
            R.id.navigation_home,
            R.id.navigation_workout,
            R.id.navigation_routine,
            R.id.navigation_notifications
        )
    )}

    private lateinit var binding: ActivityMainBinding
    private var navBarShown = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        setupNavControllerListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun setupNavControllerListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_routine_detail -> {
                    Log.d("TEST", "RoutineDetail")
                    hideBottomNavigationView(binding.navView)
                }
            }

            if (destination.id != R.id.navigation_routine_detail && !navBarShown) {
                showBottomNavigationView(binding.navView)
            }
        }
    }

    private fun hideBottomNavigationView(bottomView: BottomNavigationView) {
        if (navBarShown) {
//            bottomView.collapse()
            bottomView.visibility = View.GONE
            navBarShown = false
        }
    }

    private fun showBottomNavigationView(bottomView: BottomNavigationView) {
        if (!navBarShown) {
//            bottomView.expand()
            bottomView.visibility = View.VISIBLE
            navBarShown = true
        }
    }
}
