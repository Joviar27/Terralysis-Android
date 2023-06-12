package com.example.terralysis.ui.authorization.splashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.terralysis.databinding.LayoutSplashScreenBinding
import com.example.terralysis.util.ViewModelFactory

class SplashScreenFragment : Fragment() {
    private var _binding: LayoutSplashScreenBinding? = null
    private val binding get() = _binding

    private var _viewModel: SplashScreenViewModel? = null
    private val viewModel get() = _viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtainViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutSplashScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAuthState()
    }

    private fun checkAuthState(){
        viewModel?.getAuthData()?.observe(viewLifecycleOwner){ auth ->
            val direction = if(auth.state){
                SplashScreenFragmentDirections.actionNavigationSplashscreenToNavigationHome()
            } else {
                SplashScreenFragmentDirections.actionNavigationSplashscreenToNavigationWelcome()
            }
            view?.findNavController()?.navigate(direction)
        }
    }

    private fun obtainViewModel() {
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel : SplashScreenViewModel by viewModels {
            factory
        }
        _viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}