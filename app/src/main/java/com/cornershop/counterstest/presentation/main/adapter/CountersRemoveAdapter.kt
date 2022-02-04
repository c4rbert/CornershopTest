package com.cornershop.counterstest.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.data.database.entities.CountersItemEntity
import com.cornershop.counterstest.databinding.ItemRemovableCounterBinding
import com.cornershop.counterstest.databinding.ItemRemoveCounterBinding
import com.cornershop.counterstest.databinding.ItemTenuredCounterBinding
import com.cornershop.counterstest.presentation.main.fragments.MainFragment
import com.cornershop.counterstest.presentation.main.viewholder.CounterRemovableViewHolder
import com.cornershop.counterstest.presentation.main.viewholder.CounterTenuredViewHolder

class CountersRemoveAdapter(var context: MainFragment, var counters: ArrayList<CountersItemEntity>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val REMOVABLE = 0
        const val TENURED = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (counters[position].removable) {
            REMOVABLE
        } else {
            TENURED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            REMOVABLE -> {
                CounterRemovableViewHolder(
                    ItemRemovableCounterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TENURED -> {
                CounterTenuredViewHolder(
                    ItemTenuredCounterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CounterRemovableViewHolder -> {
                holder.bindData(context, counters[position])
            }

            is CounterTenuredViewHolder -> {
                holder.bindData(context, counters[position])
            }
        }
    }

    override fun getItemCount(): Int = counters.size
}