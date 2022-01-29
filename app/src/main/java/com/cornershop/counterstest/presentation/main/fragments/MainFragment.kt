package com.cornershop.counterstest.presentation.main.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornershop.counterstest.R
import com.cornershop.counterstest.core.Resource
import com.cornershop.counterstest.data.database.entities.CountersItemEntity
import com.cornershop.counterstest.data.model.counters.Counters
import com.cornershop.counterstest.data.model.inccounter.IdCounter
import com.cornershop.counterstest.data.remote.MainDataSource
import com.cornershop.counterstest.databinding.MainFragmentBinding
import com.cornershop.counterstest.domain.api.CounterAPI
import com.cornershop.counterstest.presentation.main.adapter.CountersAdapter
import com.cornershop.counterstest.data.repositories.main.MainRepository
import com.cornershop.counterstest.viewmodel.MainViewModel
import gentera.yastas.yas_app_client_gestion_ventas.api.client.Client
import gentera.yastas.yas_app_client_gestion_ventas.viewmodel.factory.ViewModelFactory

class MainFragment : Fragment() {

    private val TAG: String = javaClass.simpleName

    private lateinit var binding: MainFragmentBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory(
            MainRepository(
                requireContext(),
                MainDataSource(
                    Client(requireContext()).getClient(CounterAPI::class.java) as CounterAPI
                )
            )
        )
    }

    private lateinit var countersAdapter: CountersAdapter

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        initRecyclerValues()
        setRetryListener()
        getCounters()
        setRefreshSwipeListener()
        setAddCounterListener()
        return binding.root
    }

    private fun initRecyclerValues() {
        binding.countersRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setRetryListener() {
        binding.couldNotLoadCountersContainer.retry.setOnClickListener {
            getCounters()
        }
    }

    private fun getCounters() {
        mainViewModel.getCounters().observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.i(TAG, "Getting Counters")
                    initUI()
                }

                is Resource.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.progressCircular.visibility = View.GONE
                    Log.d(TAG, "${result.data}")

                    if (result.data.size > 0) {
                        binding.itemTimesCounter.root.visibility = View.VISIBLE
                        setResultData(result.data)
                    } else {
                        binding.noCounters.root.visibility = View.VISIBLE
                    }
                }

                is Resource.Failure -> {
                    showCouldNotLoadCounters()
                    Log.e(TAG, result.exception.toString())
                }
            }
        })
    }

    private fun initUI() {
        binding.apply {
            swipeRefreshLayout.setColorSchemeColors(requireContext().getColor(R.color.orange))
            progressCircular.visibility = View.VISIBLE
            binding.noCounters.root.visibility = View.GONE
            couldNotLoadCountersContainer.root.visibility = View.GONE
        }
    }

    private fun showCouldNotLoadCounters() {
        binding.apply {
            swipeRefreshLayout.isRefreshing = false
            progressCircular.visibility = View.GONE
            couldNotLoadCountersContainer.root.visibility = View.VISIBLE
        }
    }

    private fun setResultData(data: Counters) {
        viewModel.deleteCounters()
        viewModel.insertCounters(data)
        setRecyclerData(data)
        setTotalItems(data)
        setTotalTimes(data)
    }

    private fun setRecyclerData(data: Counters) {
        countersAdapter = CountersAdapter(this, data)
        binding.countersRecyclerView.adapter = countersAdapter
        countersAdapter.notifyDataSetChanged()
    }

    private fun setTotalItems(data: Counters) {
        val itemLabel =
            if (data.size == 1) getString(R.string.one_item) else getString(R.string.n_items)
        binding.itemTimesCounter.itemCounter.text =
            String.format(itemLabel, data.size)
    }

    private fun setTotalTimes(data: Counters) {
        val totalTimes = data.sumOf { it.count }
        val timesLabel =
            if (totalTimes == 1) getString(R.string.one_time) else getString(R.string.n_times)
        binding.itemTimesCounter.timesCounter.text =
            String.format(timesLabel, totalTimes)
    }

    private fun setRefreshSwipeListener() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            getCounters()
        }
    }

    private fun setAddCounterListener() {
        binding.addCounters.setOnClickListener {
            findNavController().navigate(R.id.createItemFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun decCounter(countersItem: CountersItemEntity) {

        mainViewModel.decCounter(IdCounter(countersItem.id)).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.i(TAG, "Decrementing Counter")
                    initUI()
                }
                is Resource.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.progressCircular.visibility = View.GONE
                    if (result.data.size > 0) {
                        setResultData(result.data)
                    } else {
                        binding.noCounters.root.visibility = View.VISIBLE
                    }
                }
                is Resource.Failure -> {
                    showCouldNotLoadCounters()
                    Log.e(TAG, result.exception.toString())
                }
            }
        })
    }

    fun incCounter(countersItem: CountersItemEntity) {
        mainViewModel.incCounter(IdCounter(countersItem.id)).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.i(TAG, "Incrementing Counter")
                    initUI()
                }
                is Resource.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.progressCircular.visibility = View.GONE
                    if (result.data.size > 0) {
                        setResultData(result.data)
                    } else {
                        binding.noCounters.root.visibility = View.VISIBLE
                    }
                }
                is Resource.Failure -> {
                    showCouldNotLoadCounters()
                    Log.e(TAG, result.exception.toString())
                }
            }
        })
    }
}