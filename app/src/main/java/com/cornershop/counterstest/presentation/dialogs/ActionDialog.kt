package com.cornershop.counterstest.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cornershop.counterstest.databinding.ActionDialogBinding
import com.cornershop.counterstest.presentation.callbacks.CountersDialogListener

class ActionDialog : DialogFragment() {

    private lateinit var binding: ActionDialogBinding

    companion object {
        private lateinit var countersDialogListener: CountersDialogListener
        private var title: String? = null
        fun newInstance(
            countersDialogListener: CountersDialogListener,
            title: String,
        ): ActionDialog {
            val dialog = ActionDialog
            dialog.countersDialogListener = countersDialogListener
            dialog.title = title
            return ActionDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActionDialogBinding.inflate(LayoutInflater.from(context))
        binding.title.text = title
        isCancelable = false
        setBtnListeners()
        return binding.root
    }

    private fun setBtnListeners() {
        binding.apply {
            deleteAction.setOnClickListener {
                countersDialogListener.onContinue()
                dismiss()
            }
            cancelAction.setOnClickListener {
                countersDialogListener.onDismiss()
                dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}