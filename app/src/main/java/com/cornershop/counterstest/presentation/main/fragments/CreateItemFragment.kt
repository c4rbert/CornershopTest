package com.cornershop.counterstest.presentation.main.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cornershop.counterstest.R
import com.cornershop.counterstest.core.Resource
import com.cornershop.counterstest.data.model.insertcounter.CreateCounter
import com.cornershop.counterstest.data.remote.CreateItemDataSource
import com.cornershop.counterstest.databinding.CreateItemFragmentBinding
import com.cornershop.counterstest.domain.api.CounterAPI
import com.cornershop.counterstest.repository.creatcounter.CreateItemRepository
import com.cornershop.counterstest.util.rx.hideKeyBoard
import com.cornershop.counterstest.util.rx.showView
import com.cornershop.counterstest.viewmodel.CreateItemViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import gentera.yastas.yas_app_client_gestion_ventas.api.client.Client
import gentera.yastas.yas_app_client_gestion_ventas.viewmodel.factory.ViewModelFactory

class CreateItemFragment : Fragment() {

    private val TAG: String = javaClass.simpleName

    private lateinit var binding: CreateItemFragmentBinding

    private val createItemViewModel by viewModels<CreateItemViewModel> {
        ViewModelFactory(
            CreateItemRepository(
                this,
                CreateItemDataSource(
                    Client(requireContext()).getClient(CounterAPI::class.java) as CounterAPI
                )
            )
        )
    }

    companion object {
        fun newInstance() = CreateItemFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = CreateItemFragmentBinding.inflate(inflater, container, false)

        setCloseListener()
        setSaveCounterListener()

        return binding.root
    }

    private fun setCloseListener() {
        binding.closeAction.setOnClickListener { findNavController().navigate(R.id.mainFragment) }
    }

    private fun setSaveCounterListener() {
        binding.saveAction.setOnClickListener {
            showSaveAction(false)
            initCreateCounter()
        }
    }

    private fun initCreateCounter() {
        binding.counterName.text.toString().let {
            if (it == "") {
                showSnackbar()
            } else {
                createCounter(it)
            }
        }
    }

    private fun showSnackbar() {
        Snackbar
            .make(binding.root,
                R.string.create_counter_disclaimer,
                Snackbar.LENGTH_SHORT).addCallback(object :
                BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onShown(transientBottomBar: Snackbar?) {
                    super.onShown(transientBottomBar)
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    showSaveAction(true)
                }
            }).show()
    }

    private fun createCounter(counterName: String) {
        createItemViewModel.createCounter(CreateCounter(counterName))
            .observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.i(TAG, "Saving counter")
                    }

                    is Resource.Success -> {
                        if (result.data.size > 0) {
                            findNavController().navigate(R.id.mainFragment)
                        } else {

                        }
                    }

                    is Resource.Failure -> {
                        Log.e(TAG, result.exception.toString())
                    }
                }
            })
    }

    private fun showSaveAction(status: Boolean) {
        binding.apply {
            root.hideKeyBoard()
            showView(progressCircular, !status)
            showView(saveAction, status)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }
}