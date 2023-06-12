package com.example.terralysis.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.terralysis.R
import com.example.terralysis.databinding.LayoutAboutAppBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContent()
    }

    private fun setContent(){
        binding?.apply {
            tvAboutApp1.text = resources.getString(R.string.about_1)
            tvAboutApp2.text = resources.getString(R.string.about_2)
            tvRepoLink.setOnClickListener{
                val link = tvRepoLink.text.toString()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(link)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}