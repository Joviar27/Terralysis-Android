package com.example.terralysis.ui.profile

import android.content.res.Resources
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
import com.example.terralysis.databinding.LayoutProfileBinding
import com.example.terralysis.util.ViewModelFactory
import java.util.*
import com.example.terralysis.data.ResultState
import com.example.terralysis.ui.AuthActivity

class ProfileFragment : Fragment() {
    private var _binding: LayoutProfileBinding? = null
    private val binding get() = _binding

    private var _viewModel: ProfileViewModel? = null
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

        _binding = LayoutProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAuth()
        setContent()
        setNavigation()
    }

    private fun checkAuth(){
        viewModel?.getAuthData()?.observe(viewLifecycleOwner){ result ->
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
        //Temp - until found default pic
        binding?.ivProfile?.setImageResource(R.color.secondary)

        viewModel?.getUserData()?.observe(viewLifecycleOwner){ user ->
            binding?.tvUsername?.text = user.name
            binding?.tvEmail?.text = user.email
        }

        binding?.itemBahasa?.apply {
            mtrlListItemText.text = resources.getText(R.string.language)
            mtrlListItemSecondaryText.text = getLocaleLanguage()
        }
        binding?.itemBantuan?.apply {
            mtrlListItemIcon.setImageResource(R.drawable.outline_contact_support_24)
            mtrlListItemText.text = resources.getText(R.string.bantuan)
            mtrlListItemSecondaryText.visibility = View.GONE
        }
        binding?.itemAboutApp?.apply {
            mtrlListItemIcon.setImageResource(R.drawable.outline_info_24)
            mtrlListItemText.text = resources.getText(R.string.about_app)
            mtrlListItemSecondaryText.visibility = View.GONE
        }
    }

    private fun getLocaleLanguage(): String {
        val currentLocale: Locale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0]
        } else {
            @Suppress("Deprecation")
            Resources.getSystem().configuration.locale
        }

        return currentLocale.language
    }

    private fun setNavigation(){
        binding?.apply {
            itemBantuan.mtrlListItemNavigation.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_bantuanFragment)
            )
            itemAboutApp.mtrlListItemNavigation.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_AboutFragment)
            )
            btnLogout.setOnClickListener{ viewModel?.logout() }
        }
    }

    private fun startAuthActivity(){
        val authIntent = Intent(requireContext(), AuthActivity::class.java)
        authIntent.putExtra(AuthActivity.isSplash, false)
        startActivity(authIntent)
        requireActivity().finish()
    }

    private fun obtainViewModel() {
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel : ProfileViewModel by viewModels {
            factory
        }
        _viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}