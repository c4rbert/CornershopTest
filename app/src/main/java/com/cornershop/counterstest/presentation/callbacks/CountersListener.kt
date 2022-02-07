package com.cornershop.counterstest.presentation.callbacks

interface CountersListener {

    fun decCounter()

    fun incCounter()

    fun setRemovableItemStatus(adapterPosition: Int, b: Boolean)
}