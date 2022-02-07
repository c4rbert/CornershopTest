package com.cornershop.counterstest.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ErrorDialogBinding
import com.cornershop.counterstest.presentation.callbacks.CountersDialogListener

class ErrorDialog : DialogFragment() {

    private lateinit var binding: ErrorDialogBinding

    companion object {
        val DISMISS = 0
        val OK = 1
        private var btnType = 1
        private var countersDialogListener: CountersDialogListener? = null
        private var title: String? = null
        fun newInstance(
            countersDialogListener: CountersDialogListener?,
            title: String,
            btnType: Int = 1
        ): ErrorDialog {
            val dialog = ErrorDialog
            dialog.countersDialogListener = countersDialogListener
            dialog.title = title
            dialog.btnType = btnType
            return ErrorDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ErrorDialogBinding.inflate(LayoutInflater.from(context))
        binding.title.text = title
        isCancelable = false
        setBtnValues()
        return binding.root
    }

    private fun setBtnValues() {
        binding.apply {
            when (btnType) {
                DISMISS -> action.text = getString(R.string.dismiss)
            }
            action.setOnClickListener { dismiss() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}