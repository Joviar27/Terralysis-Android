package com.example.terralysis.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.terralysis.R
import com.example.terralysis.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavGraph()
    }

    private fun setNavGraph(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_auth) as NavHostFragment
        val navController = navHostFragment.navController
        val navInflater = navHostFragment.navController.navInflater

        val isSplash = intent?.getBooleanExtra(isSplash, true)?: true

        if(isSplash){
            navController.graph = navInflater.inflate(R.navigation.nav_graph_splash)
        }else{
            navController.graph = navInflater.inflate(R.navigation.nav_graph_auth)
        }
    }

    companion object{
        const val isSplash = "is_splash"
    }
}