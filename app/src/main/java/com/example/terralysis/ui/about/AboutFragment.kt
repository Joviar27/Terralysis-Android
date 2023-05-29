package com.example.terralysis.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.terralysis.R
import com.example.terralysis.databinding.LayoutAboutAppBinding
import com.example.terralysis.databinding.LayoutBantuanPageBinding

class AboutFragment : Fragment() {
    private var _binding: LayoutAboutAppBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LayoutAboutAppBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}