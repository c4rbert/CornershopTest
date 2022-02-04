package com.cornershop.counterstest.presentation.main.viewholder

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.data.database.entities.CountersItemEntity
import com.cornershop.counterstest.databinding.ItemCounterBinding
import com.cornershop.counterstest.presentation.main.fragments.MainFragment

class CounterViewHolder(val binding: ItemCounterBinding) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var context: MainFragment

    fun bindData(context: MainFragment, item: CountersItemEntity) {
        this.context = context

        setRemoveListener()

        setItemValues(item)

        validateEnableDecListener(item)

        setPlusListener(item)
    }

    private fun setRemoveListener() {
        binding.counterContainer.setOnLongClickListener {
            context.setRemovableItemStatus(adapterPosition, true)
            true
        }
    }

    private fun setItemValues(item: CountersItemEntity) {
        with(item) {
            binding.apply {
                counterName.text = title
                totalCounter.text = count.toString()
            }
        }
    }

    private fun validateEnableDecListener(item: CountersItemEntity) {
        with(item) {
            if (count == 0) {
                binding.apply {
                    decAction.colorFilter = getGrayFilterColor()
                    totalCounter.setTextColor(
                        ContextCompat.getColor(
                            context.requireContext(),
                            R.color.gray
                        ).hashCode()
                    )
                }
            } else {
                setDecListener(item)
            }
        }
    }

    private fun setDecListener(item: CountersItemEntity) {
        binding.decAction.setOnClickListener {
            context.decCounter(item)
        }
    }

    private fun setPlusListener(item: CountersItemEntity) {
        binding.plusAction.setOnClickListener {
            context.incCounter(item)
        }
    }

    private fun getGrayFilterColor(): PorterDuffColorFilter {
        return PorterDuffColorFilter(
            ContextCompat.getColor(
                context.requireContext(),
                R.color.gray
            ).hashCode(),
            PorterDuff.Mode.SRC_IN
        )
    }
}