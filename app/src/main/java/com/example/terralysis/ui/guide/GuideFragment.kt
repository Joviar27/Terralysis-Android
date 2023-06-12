package com.example.terralysis.ui.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.terralysis.R
import com.example.terralysis.databinding.ItemGuideBinding
import com.example.terralysis.databinding.LayoutGuideBinding

class GuideFragment: Fragment() {
    private var _binding: LayoutGuideBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LayoutGuideBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContent()
    }

    private fun setContent() {
        binding.apply {
            setContentItemGuide(
                itemGuide1,
                R.drawable.guide_1,
                R.string.title_1,
                R.string.desc_1,
                resources.getString(R.string.cdesc_prepare_sample)
            )
            setContentItemGuide(
                itemGuide2,
                R.drawable.guide_2,
                R.string.title_2,
                R.string.desc_2,
                resources.getString(R.string.cdesc_adjust_lighting)
            )
            setContentItemGuide(
                itemGuide3,
                R.drawable.guide_3,
                R.string.title_3,
                R.string.desc_3,
                resources.getString(R.string.cdesc_quality_image)
            )
        }
    }

    private fun setContentItemGuide(
        itemGuide: ItemGuideBinding,
        imgId: Int,
        titleId: Int,
        descId: Int,
        contentDesc: String
    ) {
        itemGuide.apply {
            ivGuide.setImageDrawable(ContextCompat.getDrawable(requireContext(), imgId))
            ivGuide.contentDescription = contentDesc
            tvTitle.text = resources.getString(titleId)
            tvDescription.text = resources.getString(descId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}