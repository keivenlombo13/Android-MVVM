package com.keiven.github.binding

import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("onTextSubmitted")
    fun onTextSubmitted(view: SearchView, function: ((text: String) -> Unit)?) {
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { function?.invoke(query) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}