package com.example.terralysis.ui.bantuan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.terralysis.databinding.LayoutHelpBinding
import com.example.terralysis.R
import com.example.terralysis.data.teamCC
import com.example.terralysis.data.teamMD
import com.example.terralysis.data.teamML
import com.example.terralysis.databinding.ItemDevTeamBinding

class BantuanFragment : Fragment() {
    private var _binding: LayoutHelpBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LayoutHelpBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContent()
    }

    private fun setContent(){
        setTeamInfo(binding.bantuanMdTeam, teamMD())
        setTeamInfo(binding.bantuanMlTeam, teamML())
        setTeamInfo(binding.bantuanCcTeam, teamCC())
//        binding.apply {
//            bantuanMdTeam.apply {
//                tvTeam.text = "Android Developer"
//                tvNameMember1.text = ""
//                tvNameMember2.text = ""
//                tvEmailMember1.text = ""
//                tvEmailMember2.text = ""
//            }
//            bantuanCcTeam.apply {
//                tvTeam.text = "Cloud Developer"
//                tvNameMember1.text = ""
//                tvNameMember2.text = ""
//                tvEmailMember1.text = ""
//                tvEmailMember2.text = ""
//
//            }
//            bantuanMlTeam.apply {
//                tvTeam.text = "Machine Learning Developer"
//                tvNameMember1.text = ""
//                tvNameMember2.text = ""
//                tvEmailMember1.text = ""
//                tvEmailMember2.text = ""
//            }
//        }
    }

    private fun setTeamInfo(layout: ItemDevTeamBinding, team: List<String>) {
        for(i in 0..layout.root.childCount-1){
            val view = layout.root.get(i)
            if( view is TextView){
                when(0){
                    i -> view.text = team[i]
                    i % 2 -> view.text = resources.getString(R.string.email, team[i])
                    0 -> view.text = resources.getString(R.string.name, team[i])
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}