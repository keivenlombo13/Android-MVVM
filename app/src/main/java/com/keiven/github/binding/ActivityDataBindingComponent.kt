package com.keiven.github.binding

import android.content.Context
import androidx.databinding.DataBindingComponent

class ActivityDataBindingComponent(context: Context) : DataBindingComponent {
    private val adapter = ActivityBindingAdapter(context)

    override fun getActivityBindingAdapter() = adapter
}