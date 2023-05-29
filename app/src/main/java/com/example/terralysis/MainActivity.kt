package com.example.terralysis

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.terralysis.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        setBottomNavigation(navController)
    }

    private fun setBottomNavigation(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.navView.visibility =
                if (listFrament.contains(destination.id)) View.VISIBLE else View.GONE

            binding.toolbar.apply {
                visibility =
                    if (listFrament.contains(destination.id) || destination.id == R.id.navigation_camera) View.GONE
                    else View.VISIBLE
                title = destination.label
                setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            }

        }
    }

    companion object {
        val listFrament =
            listOf(R.id.navigation_home, R.id.navigation_scan, R.id.navigation_history, R.id.navigation_profile)
    }
}