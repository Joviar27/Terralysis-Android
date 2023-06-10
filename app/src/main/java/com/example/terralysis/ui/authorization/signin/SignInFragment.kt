package com.example.terralysis.ui.authorization.signin

import android.content.Intent
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
import com.example.terralysis.databinding.LayoutSigninBinding
import com.example.terralysis.ui.MainActivity
import com.example.terralysis.util.ViewModelFactory

class SignInFragment : Fragment() {
    private var _binding: LayoutSigninBinding? = null
    private val binding get() = _binding

    private var _viewModel: SignInViewModel? = null
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
        _binding = LayoutSigninBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAuth()
        setupView()
    }

    private fun checkAuth() {
        viewModel?.getAuthData()?.observe(viewLifecycleOwner) { result ->
            if (result.state) startMainActivity()
        }
    }

    private fun setupView(){
        binding?.apply {
            btnSignin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                clearError()
                when {
                    email.isEmpty() -> binding?.etlEmail?.error = resources.getString(R.string.err_required)
                    !isValidEmail(email) -> binding?.etlEmail?.error = resources.getString(R.string.err_invalid_email)
                    password.isEmpty() -> binding?.etlPassword?.error = resources.getString(R.string.err_required)
                    password.length <8 -> binding?.etlPassword?.error = resources.getString(R.string.err_invalid_pass_1)
                    password.contains(" ") -> binding?.etlPassword?.error = resources.getString(R.string.err_invalid_pass_2)
                    else -> signIn(email, password)
                }
            }
            tvSignUp.setOnClickListener {
                val toSignUp = SignInFragmentDirections.actionNavigationSigninToNavigationSignup()
                view?.findNavController()?.navigate(toSignUp)
            }
        }
    }

    private fun clearError(){
        binding?.apply {
            etlEmail.error = null
            etlPassword.error = null
        }
    }

    private fun signIn(email: String, password: String) {
        viewModel?.signIn(email, password)?.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Error -> {
                    showLoading(false)
                    showToast(resources.getString(R.string.signin_failed))
                }
                is ResultState.Success -> {
                    showLoading(false)
                    showToast(resources.getString(R.string.signin_succcess))
                }
            }
        }
    }

    private fun showLoading(isLoading : Boolean){
        binding?.pbLoading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message : String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: String) =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun startMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun obtainViewModel() {
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel : SignInViewModel by viewModels {
            factory
        }
        _viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}