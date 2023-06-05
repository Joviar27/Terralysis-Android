package com.example.terralysis.ui.authorization.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.terralysis.R
import com.example.terralysis.data.ResultState
import com.example.terralysis.databinding.LayoutSignupBinding
import com.example.terralysis.util.ViewModelFactory

class SignUpFragment : Fragment() {
    private var _binding: LayoutSignupBinding? = null

    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LayoutSignupBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnSignin.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                submitForm(name, email, password)
            }
        }
    }

    private fun submitForm(name: String, email: String, password: String){
        when {
            name.isEmpty() -> binding.etName.error =
                resources.getString(R.string.err_required)
            email.isEmpty() -> binding.etlEmail.error =
                resources.getString(R.string.err_required)
            !isValidEmail(email) -> binding.etlEmail.error =
                resources.getString(R.string.err_invalid_email)
            password.isEmpty() -> binding.etlPassword.error =
                resources.getString(R.string.err_required)
            else -> {
                viewModel.signUp(name, email, password).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is ResultState.Loading -> binding.pbLoading.visibility = View.VISIBLE
                        is ResultState.Error -> {
                            binding.pbLoading.visibility = View.GONE
                            Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                        }
                        is ResultState.Success -> {
                            binding.pbLoading.visibility = View.GONE
                            if(result.data.error == false){
                                Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isValidEmail(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun findNavController(): NavController {
        val fragmentManager = requireParentFragment().parentFragmentManager
        val navHostFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_auth) as NavHostFragment
        val navController = navHostFragment.navController
        return navController
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}