package com.example.terralysis.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.terralysis.R
import com.example.terralysis.data.ResultState
import com.example.terralysis.data.local.entity.ScanEntity
import com.example.terralysis.databinding.LayoutDetailBinding
import com.example.terralysis.util.ViewModelFactory

class DetailFragment : Fragment() {
    private var _binding: LayoutDetailBinding? = null
    private val binding get() = _binding

    private var _viewModel: DetailViewModel? = null
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
        _binding = LayoutDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scanDetail = DetailFragmentArgs.fromBundle(arguments as Bundle).scanDetail

        viewModel?.getUserData()?.observe(viewLifecycleOwner){ user ->
            getScanDetail(user.userId, scanDetail.id)
        }

        setupView(scanDetail)
    }

    private fun setupView(scanDetail : ScanEntity){
        binding?.apply {
            tvDetailLabel.text = resources.getString(R.string.label,scanDetail.name, scanDetail.shortDesc)
            tvDetailLongdesc.text = scanDetail.longDesc
            tvDetailProperty.text = scanDetail.property
            tvDetailSpread.text = scanDetail.spread
            tvDetailPhysicalChar.text = scanDetail.physicalCar
            tvDetailMorphologyChar.text = scanDetail.morphologyChar
            tvDetailChemicalChar.text = scanDetail.chemicalChar

            ivDetail.let {
                Glide.with(requireActivity())
                    .load(scanDetail.uri)
                    .placeholder(R.color.primaryContainer)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(it)
            }
        }
    }

    private fun getScanDetail(userId : String, imageId : String){
        viewModel?.getScanDetail(userId, imageId)?.observe(viewLifecycleOwner){ result ->
            when (result) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Error -> {
                    showToast(resources.getString(R.string.err_api_problem))
                    Log.e(TAG, result.error)
                    showLoading(false)
                }
                is ResultState.Success -> {
                    showLoading(false)
                    val data = result.data
                    setupView(data)
                }
            }
        }
    }

    private fun showLoading(isLoading : Boolean){
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message : String){
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel() {
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel : DetailViewModel by viewModels {
            factory
        }
        _viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val TAG = "DetailFragment"
    }
}