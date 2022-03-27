package com.keiven.github.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val creators: Map<Class<out ViewModel>, ViewModel>
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value

        try {
            @Suppress("UNCHECKED_CAST")
            return creator as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}