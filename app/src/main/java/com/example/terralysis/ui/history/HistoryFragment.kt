package com.example.terralysis.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terralysis.R
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

        setRVComponent()

        loadHistory()

        binding?.btnRefresh?.setOnClickListener{
            loadHistory()
        }
    }

    private fun loadHistory(){
        showError(false)
        viewModel?.getUserData()?.observe(viewLifecycleOwner){ user ->
            getHistory(user.userId)
        }
    }

    private fun setRVComponent(){
        _historyAdapter = HistoryAdapter(object : HistoryAdapter.OnItemClicked{
            override fun onClicked(scanDetail: ScanEntity) {
                navigateToDetail(scanDetail)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvRiwayat?.apply {
            adapter = historyAdapter
            setLayoutManager(layoutManager)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getHistory(userId : String) {
        viewModel?.getHistory(userId)?.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }
                is ResultState.Error -> {
                    showToast(resources.getString(R.string.err_api_problem))
                    Log.e(TAG, result.error)
                    showLoading(false)
                    showError(true)
                }
                is ResultState.Success -> {
                    showLoading(false)
                    val data = result.data
                    if(data.isEmpty()){
                        showEmpty(true)
                    } else{
                        showEmpty(false)
                        historyAdapter?.submitList(data)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading : Boolean){
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun showEmpty(isEmpty : Boolean){
        binding?.apply {
            emptyView?.apply {
                visibility = if (isEmpty) View.VISIBLE else View.GONE
                text = resources.getString(R.string.empty_history)
            }
            btnRefresh.visibility = View.GONE
        }
    }

    private fun showError(isError : Boolean){
        binding?.apply {
            emptyView?.apply {
                visibility = if (isError) View.VISIBLE else View.GONE
                text = resources.getString(R.string.failed_load_history)
            }
            btnRefresh.visibility = if (isError) View.VISIBLE else View.GONE
        }
    }

    private fun showToast(message : String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
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

    companion object{
        private const val TAG = "HistoryFragment"
    }
}