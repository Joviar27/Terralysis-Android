package com.example.terralysis.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.terralysis.R
import com.example.terralysis.databinding.LayoutHomeBinding

class HomeFragment : Fragment() {

    private var _binding: LayoutHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LayoutHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToCamera()
        navigateToAbout()
    }

    private fun navigateToCamera(){
        binding.btnScan.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_navigation_camera)
        )
    }

    private fun navigateToAbout(){
        binding.btnAboutApps.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_navigation_guide)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}