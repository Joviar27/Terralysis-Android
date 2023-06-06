package com.example.terralysis.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.terralysis.data.ResultState
import com.example.terralysis.data.local.entity.ScanEntity
import com.example.terralysis.databinding.LayoutHistoryBinding
import com.example.terralysis.util.HistoryAdapter
import com.example.terralysis.util.ViewModelFactory

class HistoryFragment : Fragment() {
    private var _binding: LayoutHistoryBinding? = null
    private val binding get() = _binding

    private var _historyAdapter : HistoryAdapter? = null
    private val historyAdapter get() = _historyAdapter

    private var _viewModel: HistoryViewModel? = null
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
        _binding = LayoutHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _historyAdapter = HistoryAdapter(object : HistoryAdapter.OnItemClicked{
            override fun onClicked(scanDetail: ScanEntity) {
                navigateToDetail(scanDetail)
            }
        })

        viewModel?.getUserData()?.observe(viewLifecycleOwner){ user ->
            getHistory(user.userId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getHistory(userId : String) {
        viewModel?.getHistory(userId)?.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        context,
                        result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ResultState.Success -> {
                    showLoading(false)
                    val data = result.data
                    historyAdapter?.submitList(data)
                }
            }
        }
    }

    private fun showLoading(isLoading : Boolean){
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel() {
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel : HistoryViewModel by viewModels {
            factory
        }
        _viewModel = viewModel
    }

    private fun navigateToDetail(scan : ScanEntity) {
        val toDetailScan = HistoryFragmentDirections.actionNavigationHistoryToNavigationDetail(scan)
        toDetailScan.scanDetail = scan
        view?.findNavController()?.navigate(toDetailScan)
    }
}