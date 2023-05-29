package com.example.terralysis.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.terralysis.R
import com.example.terralysis.databinding.ActivityCameraBinding

class CameraFragment : Fragment() {
    private var _binding: ActivityCameraBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ActivityCameraBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backNavigation()
        takePicture()
    }

    private fun takePicture(){
        binding.btnCapture.setOnClickListener(
            //logic for taking a picture
            Navigation.createNavigateOnClickListener(R.id.action_CameraFragment_to_DetailFragment)
        )
    }
    private fun backNavigation(){
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}