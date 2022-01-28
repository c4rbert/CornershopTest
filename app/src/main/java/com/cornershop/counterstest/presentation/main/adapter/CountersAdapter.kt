package com.cornershop.counterstest.presentation.main.adapter

import android.app.Activity
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.data.model.counters.CountersItem
import com.cornershop.counterstest.databinding.ItemCounterBinding
import com.cornershop.counterstest.presentation.main.fragments.MainFragment

class CountersAdapter(var context: MainFragment, var counteres: ArrayList<CountersItem>) :
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

                    if (count == 0) {
                        val filter: ColorFilter =
                            PorterDuffColorFilter(ContextCompat.getColor(context.requireContext(),
                                R.color.gray).hashCode(),
                                PorterDuff.Mode.SRC_IN)
                        decAction.colorFilter = filter
                        totalCounter.setTextColor(ContextCompat.getColor(context.requireContext(),
                            R.color.gray).hashCode())
                    } else {
                        decAction.setOnClickListener {
                            context.decCounter(counteres[position])
                        }
                    }

                    plusAction.setOnClickListener {
                        context.incCounter(counteres[position])
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int = counteres.size
}