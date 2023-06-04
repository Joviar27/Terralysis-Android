package com.example.terralysis.ui.authorization.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.terralysis.R
import com.example.terralysis.databinding.LayoutWelcomeBinding

class WelcomeFragment : Fragment() {
    private var _binding: LayoutWelcomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LayoutWelcomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInOrSignUp()
    }

    fun signInOrSignUp(){
        binding.apply {
            btnSignin.setOnClickListener(
                Navigation.createNavigateOnClickListener( R.id.action_welcomeFragment_to_signInFragment )
            )
            btnSignup.setOnClickListener(
                Navigation.createNavigateOnClickListener( R.id.action_welcomeFragment_to_signUpFragment)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}