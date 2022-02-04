package com.cornershop.counterstest.presentation.main.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.data.database.entities.CountersItemEntity
import com.cornershop.counterstest.databinding.ItemRemovableCounterBinding
import com.cornershop.counterstest.presentation.main.fragments.MainFragment

class CounterRemovableViewHolder(val binding: ItemRemovableCounterBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var context: MainFragment

    fun bindData(context: MainFragment, item: CountersItemEntity) {
        this.context = context

        binding.counterNameContainer.setOnClickListener {
            context.setRemovableItemStatus(adapterPosition, false)
        }
    }
}