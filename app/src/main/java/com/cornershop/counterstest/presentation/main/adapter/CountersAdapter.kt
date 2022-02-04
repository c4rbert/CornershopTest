package com.cornershop.counterstest.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.data.database.entities.CountersItemEntity
import com.cornershop.counterstest.databinding.ItemCounterBinding
import com.cornershop.counterstest.presentation.main.fragments.MainFragment
import com.cornershop.counterstest.presentation.main.viewholder.CounterViewHolder

class CountersAdapter(var context: MainFragment, var counteres: ArrayList<CountersItemEntity>) :
    RecyclerView.Adapter<CounterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterViewHolder {
        return CounterViewHolder(ItemCounterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: CounterViewHolder, position: Int) {
        with(holder) {
            bindData(context, counteres[position])
        }
    }

    override fun getItemCount(): Int = counteres.size
}