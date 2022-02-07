package com.cornershop.counterstest.presentation.main.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.data.database.entities.CountersItemEntity
import com.cornershop.counterstest.databinding.ItemRemovableCounterBinding
import com.cornershop.counterstest.presentation.callbacks.CountersListener

class CounterRemovableViewHolder(val binding: ItemRemovableCounterBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var countersListener: CountersListener

    fun bindData(countersListener: CountersListener, item: CountersItemEntity) {
        this.countersListener = countersListener
        setItemValues(item)
        setRemoveListener()
    }

    private fun setItemValues(item: CountersItemEntity) {
        binding.counterName.text = item.title
    }

    private fun setRemoveListener() {
        binding.counterNameContainer.setOnClickListener {
            countersListener.setRemovableItemStatus(adapterPosition, false)
        }
    }
}