package com.example.terralysis.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.terralysis.R
import com.example.terralysis.data.ResultState
import com.example.terralysis.databinding.LayoutProfilePageBinding
import com.example.terralysis.ui.AuthActivity
import com.example.terralysis.util.ViewModelFactory

class ProfileFragment : Fragment() {
    private var _binding: LayoutProfilePageBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels() {
        ViewModelFactory.getInstance(requireContext())
    }

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

        checkAuth()
        setContent()
        setNavigation()
    }

    private fun checkAuth(){
        viewModel.getAuthData().observe(viewLifecycleOwner){ result ->
            when(result){
                is ResultState.Loading -> {}
                is ResultState.Error -> {
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                is ResultState.Success -> if(result.data?.state == false) startAuthActivity()
            }
        }
    }

    private fun setContent(){
        viewModel.getUserData().observe(viewLifecycleOwner){ result ->
            when (result){
                is ResultState.Loading ->{ }
                is ResultState.Error ->{ }
                is ResultState.Success -> {
                    result.data.let {
                        binding.tvEmail.text = it?.email
                        binding.tvUsername.text = it?.name
                    }
                }
            }
        }

        binding.itemBahasa.apply {
            mtrlListItemText.text = resources.getText(R.string.language)
            mtrlListItemSecondaryText.text = "Indonesia"
        }
        binding.itemBantuan.apply {
            mtrlListItemIcon.setImageResource(R.drawable.outline_contact_support_24)
            mtrlListItemText.text = resources.getText(R.string.bantuan)
            mtrlListItemSecondaryText.visibility = View.GONE
        }
        binding.itemAboutApp.apply {
            mtrlListItemIcon.setImageResource(R.drawable.outline_info_24)
            mtrlListItemText.text = resources.getText(R.string.about_app)
            mtrlListItemSecondaryText.visibility = View.GONE
        }
    }

    private fun setNavigation(){
        binding.apply {
            itemBantuan.mtrlListItemNavigation.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_bantuanFragment)
            )
            itemAboutApp.mtrlListItemNavigation.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_AboutFragment)
            )
            btnLogout.setOnClickListener{ viewModel.logout() }
        }
    }

    private fun startAuthActivity(){
        val authIntent = Intent(requireContext(), AuthActivity::class.java)
        authIntent.putExtra(AuthActivity.isSplash, false)
        startActivity(authIntent)
        requireActivity().finish()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}