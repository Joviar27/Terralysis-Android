package com.example.terralysis.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.terralysis.R
import com.example.terralysis.data.local.entity.ScanEntity
import com.example.terralysis.databinding.LayoutDetailBinding

class DetailFragment : Fragment() {
    private var _binding: LayoutDetailBinding? = null
    private val binding get() = _binding

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
        setupView(scanDetail)
    }

    private fun setupView(scanDetail : ScanEntity){
        binding?.tvDetailLabel?.text = scanDetail.name
        binding?.tvDetailShortDesc?.text = scanDetail.shortDesc
        binding?.tvDetailLongDesc?.text = scanDetail.longDesc
        binding?.tvDetailLongDesc2?.text = scanDetail.longDesc

        binding?.ivDetail?.let {
            Glide.with(requireActivity())
                .load(scanDetail.uri)
                .placeholder(R.color.primaryContainer)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}