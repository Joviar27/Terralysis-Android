package com.example.terralysis.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.terralysis.R
import com.example.terralysis.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insetsController = window.insetsController
            insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            @Suppress("deprecation")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setNavGraph()
    }

    private fun setNavGraph() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_auth) as NavHostFragment
        val navController = navHostFragment.navController
        val navInflater = navHostFragment.navController.navInflater

        val isSplash = intent?.getBooleanExtra(isSplash, true) ?: true

        if (isSplash) {
            navController.graph = navInflater.inflate(R.navigation.nav_graph_splash)
        } else {
            navController.graph = navInflater.inflate(R.navigation.nav_graph_auth)
        }
    }

    companion object {
        const val isSplash = "is_splash"
    }
}