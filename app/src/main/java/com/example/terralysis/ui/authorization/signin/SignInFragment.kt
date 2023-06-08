package com.example.terralysis.ui.authorization.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.terralysis.R
import com.example.terralysis.data.ResultState
import com.example.terralysis.databinding.LayoutSigninBinding
import com.example.terralysis.ui.MainActivity
import com.example.terralysis.util.ViewModelFactory

class SignInFragment : Fragment() {
    private var _binding: LayoutSigninBinding? = null

    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LayoutSigninBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAuth()
        binding.apply {
            btnSignin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                submitForm(email, password)
            }
        }
    }

    private fun checkAuth() {
        viewModel.getAuthData().observe(viewLifecycleOwner) { result ->
            if (result.state) startMainActivity()
        }
    }

    private fun submitForm(email: String, password: String) {
        when {
            email.isEmpty() -> binding.etlEmail.error =
                resources.getString(R.string.err_required)
            !isValidEmail(email) -> binding.etlEmail.error =
                resources.getString(R.string.err_invalid_email)
            password.isEmpty() -> binding.etlPassword.error =
                resources.getString(R.string.err_required)
            else -> {
                viewModel.signIn(email, password).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is ResultState.Loading -> binding.pbLoading.visibility = View.VISIBLE
                        is ResultState.Error -> {
                            binding.pbLoading.visibility = View.GONE
                            Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT)
                                .show()
                        }
                        is ResultState.Success -> binding.pbLoading.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun isValidEmail(email: String) =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun startMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}