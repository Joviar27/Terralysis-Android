package com.example.terralysis.ui.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.terralysis.R
import com.example.terralysis.data.ResultState
import com.example.terralysis.data.local.entity.ScanEntity

import com.example.terralysis.databinding.LayoutCameraBinding
import com.example.terralysis.util.ViewModelFactory
import com.example.terralysis.util.createTempFile
import com.example.terralysis.util.rotateImage
import com.example.terralysis.util.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CameraFragment : Fragment() {
    private var _binding: LayoutCameraBinding? = null
    private val binding get() = _binding

    private var _viewModel: CameraViewModel? = null
    private val viewModel get() = _viewModel

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    private var file: File? = null

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                startCamera()
            } else {
                // Permissions not granted
                showToast("Tidak mendapatkan izin")
                view?.findNavController()?.navigateUp()
            }
        }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireContext())
                rotateImage(myFile.absolutePath)
                file = myFile
                uploadImage()
            }
        } else {
            showToast(resources.getString(R.string.failed_gallery_message))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtainViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutCameraBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) requestPermissionsLauncher.launch(REQUIRED_PERMISSIONS)
        else startCamera()

        binding?.apply {
            btnCapture.setOnClickListener { takePicture() }
            btnSwitch.setOnClickListener { switchCamera() }
            btnUpload.setOnClickListener { startGallery() }
            btnBack.setOnClickListener { view.findNavController().navigateUp() }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding?.viewFinder?.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                showToast("Gagal Memunculkan kamera")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun switchCamera() {
        cameraSelector =
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
        startCamera()
    }

    private fun takePicture() {
        val imageCapture = imageCapture ?: return
        val photoFile = createTempFile(requireContext())

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    rotateImage(photoFile.absolutePath)
                    file = photoFile
                    uploadImage()
                }

                override fun onError(exception: ImageCaptureException) {
                    showToast(resources.getString(R.string.failed_capture_message))
                }
            })
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.chooser))
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        if (file != null) {
            val myFile = file as File

            //For later on if need compressing
            //val compressedFile = reduceFileImage(myFile)

            val requestImageFile = myFile.asRequestBody("file/jpeg".toMediaTypeOrNull())

            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                myFile.name,
                requestImageFile
            )

            viewModel?.getUserData()?.observe(viewLifecycleOwner){ user ->
                viewModel?.requestScan(imageMultipart, user.userId)?.observe(viewLifecycleOwner) {
                    handleScanResult(it)
                }
            }
        }
    }

    private fun handleScanResult(result: ResultState<ScanEntity>) {
        when (result) {
            is ResultState.Loading -> {
                showLoading(true)
            }
            is ResultState.Error -> {
                showLoading(false)
                showToast(resources.getString(R.string.failed_upload_message))
                Log.e(TAG, result.error)
            }
            is ResultState.Success -> {
                showLoading(false)
                //Send image id to detail page
                navigateToDetail(result.data)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressContainer?.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding?.progressMessage?.text = resources.getString(R.string.process_message)
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun obtainViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: CameraViewModel by viewModels {
            factory
        }
        _viewModel = viewModel
    }

    //temp - navigate to detail
    private fun navigateToDetail(scan: ScanEntity) {
        val toDetailScan = CameraFragmentDirections.actionCameraFragmentToDetailFragment(scan)
        toDetailScan.scanDetail = scan
        view?.findNavController()?.navigate(toDetailScan)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val TAG = "CameraFragment"
    }
}