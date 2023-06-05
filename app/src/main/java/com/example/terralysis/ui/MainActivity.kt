package com.example.terralysis.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.terralysis.R
import com.example.terralysis.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)

        setBottomNavigation(navController)
    }

    private fun setBottomNavigation(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.navView.visibility =
                if (listFrament.contains(destination.id)) View.VISIBLE else View.GONE

            binding.toolbar.apply {
                visibility =
                    if (listFrament.contains(destination.id)) View.GONE
                    else View.VISIBLE
                title = destination.label
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }

        }
    }

    companion object {
        val listFrament =
            listOf(
                R.id.navigation_home,
                R.id.navigation_scan,
                R.id.navigation_history,
                R.id.navigation_profile
            )
    }
}