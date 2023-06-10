package com.example.terralysis.ui.authorization.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.terralysis.R
import com.example.terralysis.data.ResultState
import com.example.terralysis.databinding.LayoutSignupBinding
import com.example.terralysis.util.ViewModelFactory

class SignUpFragment : Fragment() {
    private var _binding: LayoutSignupBinding? = null
    private val binding get() = _binding

    private val viewModel: SignUpViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutSignupBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView(){
        binding?.apply {
            btnSignin.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                clearError()
                when {
                    name.isEmpty() -> binding?.etlName?.error = resources.getString(R.string.err_required)
                    email.isEmpty() -> binding?.etlEmail?.error = resources.getString(R.string.err_required)
                    !isValidEmail(email) -> binding?.etlEmail?.error = resources.getString(R.string.err_invalid_email)
                    password.isEmpty() -> binding?.etlPassword?.error = resources.getString(R.string.err_required)
                    password.length <8 -> binding?.etlPassword?.error = resources.getString(R.string.err_invalid_pass_1)
                    password.contains(" ") -> binding?.etlPassword?.error = resources.getString(R.string.err_invalid_pass_2)
                    else -> signUp(name, email, password)
                }
            }
            tvSignIn.setOnClickListener {
                navigateToSignIn()
            }
        }
    }

    private fun navigateToSignIn(){
        val toSignIn = SignUpFragmentDirections.actionNavigationSignupToNavigationSignin()
        view?.findNavController()?.navigate(toSignIn)
    }

    private fun clearError(){
        binding?.apply {
            etlEmail.error = null
            etlPassword.error = null
            etlName.error = null
        }
    }

    private fun signUp(name: String, email: String, password: String){
        viewModel.signUp(name, email, password).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Error -> {
                    showLoading(false)
                    showToast(requireContext(), resources.getString(R.string.signup_failed))
                }
                is ResultState.Success -> {
                    showLoading(false)
                    showToast(requireContext(), resources.getString(R.string.signup_succcess))
                    navigateToSignIn()
                }
            }
        }
    }

    private fun showLoading(isLoading : Boolean){
        binding?.pbLoading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(context: Context, message : String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}