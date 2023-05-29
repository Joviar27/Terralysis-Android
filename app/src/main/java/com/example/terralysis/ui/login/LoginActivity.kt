package com.example.terralysis.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.terralysis.databinding.LayoutSigninBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: LayoutSigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}