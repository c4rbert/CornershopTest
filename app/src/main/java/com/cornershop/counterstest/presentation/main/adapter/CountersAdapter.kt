package com.cornershop.counterstest.presentation.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.data.model.counters.CountersItem
import com.cornershop.counterstest.databinding.ItemCounterBinding

class CountersAdapter(var context: Context, var counteres: ArrayList<CountersItem>) :
    RecyclerView.Adapter<CountersAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemCounterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCounterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(counteres[position]) {
                binding.apply {
                    counterName.text = title
                    totalCounter.text = count.toString()
                }
            }
        }
    }

    override fun getItemCount(): Int = counteres.size
}