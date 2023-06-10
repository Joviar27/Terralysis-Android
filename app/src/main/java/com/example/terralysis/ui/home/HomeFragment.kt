package com.example.terralysis.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.terralysis.R
import com.example.terralysis.databinding.LayoutHomeBinding
import com.example.terralysis.ui.AuthActivity
import com.example.terralysis.util.CarouselAdapter
import com.example.terralysis.util.ViewModelFactory
import com.google.android.material.carousel.CarouselLayoutManager

class HomeFragment : Fragment() {

    private var _binding: LayoutHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LayoutHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAuth()
        setupView()
        setUser()
        navigateToCamera()
        navigateToGuide()
    }

    private fun checkAuth() {
        viewModel.getAuthData().observe(viewLifecycleOwner) { result ->
            if (!result.state) startAuthActivity()
        }
    }

    private fun setUser() {
        viewModel.getUserData().observe(viewLifecycleOwner) { result ->
            binding.tvWelcomeName.text = resources.getString(R.string.home_welcome_message, result.name)
        }
    }

    private fun setupView(){
        val imageList: List<Int> = listOf(
            R.drawable.carousel_1,
            R.drawable.carousel_2,
            R.drawable.carousel_3
        )
        val adapter = CarouselAdapter(imageList)
        binding.rvCarousel.layoutManager = CarouselLayoutManager()
        binding.rvCarousel.adapter = adapter
    }

    private fun startAuthActivity() {
        val authIntent = Intent(requireContext(), AuthActivity::class.java)
        authIntent.putExtra(AuthActivity.isSplash, false)
        startActivity(authIntent)
        requireActivity().finish()
    }

    private fun navigateToCamera() {
        binding.btnScan.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_navigation_camera)
        )
    }

    private fun navigateToGuide() {
        binding.btnAboutApps.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_navigation_guide)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}