package com.cornershop.counterstest.presentation.main.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
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
import com.cornershop.counterstest.domain.worker.DeleteWorker
import com.cornershop.counterstest.presentation.callbacks.CountersDialogListener
import com.cornershop.counterstest.presentation.callbacks.CountersListener
import com.cornershop.counterstest.presentation.dialogs.ActionDialog
import com.cornershop.counterstest.presentation.dialogs.ErrorDialog
import com.cornershop.counterstest.presentation.main.adapter.CountersRemoveAdapter
import com.cornershop.counterstest.util.rx.showView
import com.cornershop.counterstest.viewmodel.MainViewModel
import gentera.yastas.yas_app_client_gestion_ventas.api.client.Client
import gentera.yastas.yas_app_client_gestion_ventas.viewmodel.factory.ViewModelFactory
import kotlinx.coroutines.launch
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay

class MainFragment : Fragment(), CountersListener, CountersDialogListener {

    private val TAG: String = javaClass.simpleName

    private lateinit var binding: MainFragmentBinding
    private var counters: Counters? = null
    private var countersAux: ArrayList<CountersItemEntity>? = null
    private var countersToDelete: ArrayList<CountersItemEntity>? = null

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
    private lateinit var countersRemoveAdapter: CountersRemoveAdapter

    companion object {
        fun newInstance() = MainFragment()
    }

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
        setCloseRemoveActionListener()
        setSearchListener()
        setDeleteCountersListener()
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

    private fun validateData(result: Counters) {
        hideProgress()
        mainViewModel.deleteCounters()
        if (result.size > 0) {
            binding.itemTimesCounter.root.visibility = View.VISIBLE
            counters = result
            countersAux = counters as ArrayList<CountersItemEntity>
            setResultData(countersAux!!)
        } else {
            showNoCountersAvailable()
        }
    }

    private fun showNoCountersAvailable() {
        binding.apply {
            showView(mainSearchBar, false)
            showView(noCounters.root, true)
            showView(addCounters, true)
            showView(itemTimesCounter.root, false)
        }
    }

    private fun hideProgress() {
        binding.swipeRefreshLayout.isRefreshing = false
        showView(binding.mainProgressCircular, false)
    }

    private fun getCounters() {
        mainViewModel.getCounters().observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.i(TAG, "Getting Counters")
                    initUI()
                }

                is Resource.Success -> {
                    validateData(result.data)
                }

                is Resource.Failure -> {
                    hideProgress()
                    validateLocalData()
                    Log.e(TAG, result.exception.toString())
                }
            }
        })
    }

    private fun validateLocalData() {
        mainViewModel.viewModelScope.launch {
            val localCounters = mainViewModel.getSavedCounters()
            if (localCounters!!.size > 0) {
                binding.swipeRefreshLayout.isRefreshing = false
                showView(binding.itemTimesCounter.root, true)
                countersAux = localCounters as ArrayList<CountersItemEntity>
                setResultData(countersAux!!)
            } else {
                showCouldNotLoadCounters()
            }
        }
    }

    private fun initUI() {
        binding.apply {
            mainSearchBar.setQuery("", false)
            mainSearchBar.isIconified = true
            mainSearchBar.clearFocus()
            showView(mainProgressCircular, true)
            showView(noCounters.root, false)
            showView(couldNotLoadCountersContainer.root, false)
        }
    }

    private fun showCouldNotLoadCounters() {
        binding.apply {
            swipeRefreshLayout.isRefreshing = false
            showView(mainSearchBar, false)
            showView(itemTimesCounter.root, false)
            showView(mainProgressCircular, false)
            showView(couldNotLoadCountersContainer.root, true)
        }
    }

    private fun setResultData(counters: ArrayList<CountersItemEntity>) {
        mainViewModel.insertCounters(counters)
        setRecyclerData(counters)
        setTotalItems(counters)
        setTotalTimes(counters)
    }

    private fun setRecyclerData(data: ArrayList<CountersItemEntity>) {
        countersAdapter = CountersAdapter(this, data)
        binding.countersRecyclerView.adapter = countersAdapter
        countersAdapter.notifyDataSetChanged()
    }

    private fun setTotalItems(data: ArrayList<CountersItemEntity>) {
        val itemLabel =
            if (data.size == 1) getString(R.string.one_item) else getString(R.string.n_items)
        binding.itemTimesCounter.itemCounter.text =
            String.format(itemLabel, data.size)
    }

    private fun setTotalTimes(data: ArrayList<CountersItemEntity>) {
        val totalTimes = data.sumOf { it.count }
        val timesLabel =
            if (totalTimes == 1) getString(R.string.one_time) else getString(R.string.n_times)
        binding.itemTimesCounter.timesCounter.text =
            String.format(timesLabel, totalTimes)
    }

    private fun setRefreshSwipeListener() {
        binding.apply {
            swipeRefreshLayout.setColorSchemeColors(requireContext().getColor(R.color.orange))
            swipeRefreshLayout.setOnRefreshListener {
                binding.swipeRefreshLayout.isRefreshing = true
                getCounters()
            }
        }
    }

    private fun setAddCounterListener() {
        binding.addCounters.setOnClickListener {
            findNavController().navigate(R.id.createItemFragment)
        }
    }

    private fun setCloseRemoveActionListener() {
        binding.itemRemoveCounter.closeAction.setOnClickListener {
            resetUI()
        }
    }

    private fun resetUI() {
        showMainUI(true)
        getCounters()
    }

    private fun setSearchListener() {
        binding.mainSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                countersAux =
                    counters?.let { it.filter { it.title.contains(newText, true) } as ArrayList }
                countersAux?.let {
                    setRecyclerData(it)
                    if (it.size == 0) {
                        showUIOnSearch(false)
                    } else {
                        showUIOnSearch(true)
                    }
                }
                return false
            }
        })
    }

    private fun setDeleteCountersListener() {
        binding.itemRemoveCounter.deleteAction.setOnClickListener {
            val counterToDelete =
                countersAux?.filter { it.removable } as ArrayList<CountersItemEntity>
            initDeleteCounters(counterToDelete)
        }
    }

    private fun showUIOnSearch(visible: Boolean) {
        binding.apply {
            showView(itemTimesCounter.root, visible)
            showView(addCounters, visible)
            showView(noResults.root, !visible)
        }
    }

    fun decCounter(countersItem: CountersItemEntity) {
        mainViewModel.decCounter(IdCounter(countersItem.id)).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.i(TAG, "Decrementing Counter")
                    initUI()
                }
                is Resource.Success -> {
                    validateData(result.data)
                }
                is Resource.Failure -> {
                    showCouldNotUpdateCounter(
                        getString(
                            R.string.error_updating_counter_title,
                            countersItem.title,
                            (countersItem.count - 1)
                        )
                    )
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
                    validateData(result.data)
                }
                is Resource.Failure -> {
                    showCouldNotUpdateCounter(
                        getString(
                            R.string.error_updating_counter_title,
                            countersItem.title,
                            (countersItem.count + 1)
                        )
                    )
                    Log.e(TAG, result.exception.toString())
                }
            }
        })
    }

    private fun showCouldNotUpdateCounter(title: String) {
        val errorDialog = ErrorDialog.newInstance(null, title, ErrorDialog.DISMISS)
        activity?.supportFragmentManager?.let { errorDialog!!.show(it, errorDialog!!.tag) }
    }

    fun serializeToJson(countersToDelete: ArrayList<CountersItemEntity>): String? {
        val gson = Gson()
        return gson.toJson(countersToDelete)
    }

    private fun initDeleteCounters(countersToDelete: ArrayList<CountersItemEntity>) {
        this.countersToDelete = countersToDelete
        var deleteMessage = if (countersToDelete.size > 1) {
            String.format(getString(R.string.delete_many_counters_question))
        } else {
            String.format(getString(R.string.delete_x_question), countersToDelete[0].title)
        }
        val deleteDialog = ActionDialog.newInstance(this, deleteMessage)
        activity?.supportFragmentManager?.let { deleteDialog!!.show(it, deleteDialog!!.tag) }
    }

    private fun showMainUI(isVisible: Boolean) {
        binding.apply {
            showView(mainSearchBar, isVisible)
            showView(addCounters, isVisible)
            showView(itemRemoveCounter.root, !isVisible)
        }
    }

    fun setTotalRemovableCounters() {
        val totalRemovables = counters?.count { it.removable }
        binding.itemRemoveCounter.totalSelected.text =
            String.format(getString(R.string.n_selected), totalRemovables)
    }

    fun setRemovableAdapterValues() {
        countersRemoveAdapter = CountersRemoveAdapter(this, counters!!)
        binding.countersRecyclerView.adapter = countersRemoveAdapter
        countersAdapter.notifyDataSetChanged()
    }

    override fun decCounter() {
        TODO("Not yet implemented")
    }

    override fun incCounter() {
        TODO("Not yet implemented")
    }

    override fun setRemovableItemStatus(adapterPosition: Int, isRemovable: Boolean) {
        showMainUI(false)
        countersAux!![adapterPosition].removable = isRemovable
        setRemovableAdapterValues()
        setTotalRemovableCounters()
    }

    override fun onContinue() {
        showDeletingUI(true)
        deleteCounters(countersToDelete!![countersToDelete!!.size - 1])
    }

    override fun onDismiss() {

    }

    private fun deleteCounters(next: CountersItemEntity) {
        mainViewModel.deleteCounter(IdCounter(next.id))
            .observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.i(TAG, "Deleting Counter")
                    }
                    is Resource.Success -> {
                        countersToDelete!!.remove(next)
                        if (countersToDelete!!.size > 0) {
                            deleteCounters(countersToDelete!![countersToDelete!!.size - 1])
                        } else {
                            countersAux?.clear()
                            countersAdapter.notifyDataSetChanged()
                            GlobalScope.launch(context = Dispatchers.Main) {
                                delay(2000L)
                                showDeletingUI(false)
                                resetUI()
                            }
                        }
                    }
                    is Resource.Failure -> {
                        showCouldNotDeleteCounter()
                        Log.e(TAG, result.exception.toString())
                    }
                }
            })
    }

    private fun showDeletingUI(status: Boolean) {
        showView(binding.itemRemoveCounter.deleteAction, !status)
        showView(binding.deleting, status)
        showView(binding.itemRemoveCounter.closeAction, false)
        showView(binding.countersRecyclerView, !status)
        binding.deleting.playAnimation()
        showView(binding.itemRemoveCounter.progressCircular, status)
    }

    private fun showCouldNotDeleteCounter() {
        val title = if (countersToDelete?.size!! > 1) {
            getString(R.string.error_deleting_many_counter_title)
        } else {
            getString(R.string.error_deleting_counter_title)
        }

        val errorDialog = ErrorDialog.newInstance(null, title, ErrorDialog.DISMISS)
        activity?.supportFragmentManager?.let { errorDialog!!.show(it, errorDialog!!.tag) }
        showDeletingUI(false)
    }
}