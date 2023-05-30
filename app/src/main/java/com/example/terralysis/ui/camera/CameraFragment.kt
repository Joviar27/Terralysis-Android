package com.example.terralysis.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.terralysis.R
import com.example.terralysis.databinding.ActivityCameraBinding

class CameraFragment : Fragment() {
    private var _binding: ActivityCameraBinding? = null

    private val binding get() = _binding!!

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                // start camera
            } else {
                // Permissions not granted
                showToast("Tidak mendapatkan izin")
                requireActivity().onBackPressedDispatcher.onBackPressed()
                finishFragment()
            }
        }

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

        if(!allPermissionsGranted()) requestPermissionsLauncher.launch(REQUIRED_PERMISSIONS)

        backNavigation()
        takePicture()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun finishFragment() {
        parentFragmentManager.beginTransaction().remove(this).commit()
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

    private fun showToast(message: String){
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}