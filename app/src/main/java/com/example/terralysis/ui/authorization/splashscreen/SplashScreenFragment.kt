package com.example.terralysis.ui.authorization.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.terralysis.databinding.LayoutSplashScreenBinding
import com.example.terralysis.ui.MainActivity

class SplashScreenFragment : Fragment() {
    private var _binding: LayoutSplashScreenBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutSplashScreenBinding.inflate(inflater, container, false)

        Handler(Looper.myLooper()!!).postDelayed({
            startMainActivity()
        }, 2000L)

        return binding.root
    }

    private fun startMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}