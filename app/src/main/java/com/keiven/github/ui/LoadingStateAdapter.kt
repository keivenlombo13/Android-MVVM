package com.keiven.github.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keiven.github.R
import com.keiven.github.databinding.LoadingItemBinding

class LoadingStateAdapter: LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        return LoadingStateViewHolder.create(parent)
    }

    class LoadingStateViewHolder(
        private val binding: LoadingItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
        }

        companion object {
            fun create(parent: ViewGroup): LoadingStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_item, parent, false)

                val binding = LoadingItemBinding.bind(view)

                return LoadingStateViewHolder(
                    binding
                )
            }
        }
    }
}