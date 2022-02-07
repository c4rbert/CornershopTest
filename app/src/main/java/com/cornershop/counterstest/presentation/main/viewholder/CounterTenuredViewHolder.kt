package com.cornershop.counterstest.presentation.main.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.data.database.entities.CountersItemEntity
import com.cornershop.counterstest.databinding.ItemTenuredCounterBinding
import com.cornershop.counterstest.presentation.callbacks.CountersListener
import com.cornershop.counterstest.presentation.main.fragments.MainFragment

class CounterTenuredViewHolder(val binding: ItemTenuredCounterBinding) :
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
            countersListener.setRemovableItemStatus(adapterPosition, true)
        }
    }
}