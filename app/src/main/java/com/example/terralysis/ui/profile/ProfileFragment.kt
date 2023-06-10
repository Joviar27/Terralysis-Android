package com.example.terralysis.ui.profile

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.terralysis.R
import com.example.terralysis.databinding.LayoutProfileBinding
import com.example.terralysis.ui.AuthActivity
import com.example.terralysis.util.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

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
        setupView()
        setNavigation()
    }

    private fun checkAuth() {
        viewModel?.getAuthData()?.observe(viewLifecycleOwner) { result ->
            if (!result.state) startAuthActivity()
        }
    }

    private fun setupView() {
        //Temp - until found default pic
        binding?.ivProfile?.setImageResource(R.color.secondary)

        viewModel?.getUserData()?.observe(viewLifecycleOwner) { user ->
            binding?.tvUsername?.text = user.name
            binding?.tvEmail?.text = user.email
        }

        binding?.itemBahasa?.apply {
            mtrlListItemText.text = resources.getText(R.string.language)
            mtrlListItemSecondaryText.text = getLocaleLanguage()
        }

        binding?.itemBantuan?.apply {
            mtrlListItemIcon.setImageResource(R.drawable.outline_contact_support_24)
            mtrlListItemText.text = resources.getText(R.string.help)
            mtrlListItemSecondaryText.visibility = View.GONE
        }

        binding?.itemAboutApp?.apply {
            mtrlListItemIcon.setImageResource(R.drawable.outline_info_24)
            mtrlListItemText.text = resources.getText(R.string.about_app)
            mtrlListItemSecondaryText.visibility = View.GONE
        }
    }

    private fun getLocaleLanguage(): String {
        val currentLocale: Locale =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Resources.getSystem().configuration.locales[0]
            } else {
                @Suppress("Deprecation")
                Resources.getSystem().configuration.locale
            }

        return currentLocale.language
    }

    private fun setNavigation() {
        binding?.apply {
            itemBahasa.mtrlListItemNavigation.setOnClickListener{
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            itemBantuan.mtrlListItemNavigation.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_bantuanFragment)
            )
            itemAboutApp.mtrlListItemNavigation.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_AboutFragment)
            )
            btnLogout.setOnClickListener { showConfirmLogout() }
        }
    }

    private fun showConfirmLogout(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.logout))
            .setMessage(resources.getString(R.string.confirm_logout))
            .setNegativeButton(resources.getString(R.string.negative)) { dialog, _->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.positive)) { _, _ ->
                viewModel?.logout()
            }
            .show()
    }

    private fun startAuthActivity() {
        val authIntent = Intent(requireContext(), AuthActivity::class.java)
        authIntent.putExtra(AuthActivity.isSplash, false)
        startActivity(authIntent)
        requireActivity().finish()
    }

    private fun obtainViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: ProfileViewModel by viewModels {
            factory
        }
        _viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}