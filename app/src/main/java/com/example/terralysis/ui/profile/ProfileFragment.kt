package com.example.terralysis.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.terralysis.R
import com.example.terralysis.databinding.LayoutProfilePageBinding

class ProfileFragment : Fragment() {
    private var _binding: LayoutProfilePageBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LayoutProfilePageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemBantuan.mtrlListItemNavigation.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_bantuanFragment)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}