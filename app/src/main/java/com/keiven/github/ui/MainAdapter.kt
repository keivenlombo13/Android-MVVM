package com.keiven.github.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.keiven.github.R
import com.keiven.github.data.db.entity.Users
import com.keiven.github.databinding.ItemListBinding

class MainAdapter: PagingDataAdapter<Users.User, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        val binding = ItemListBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        getItem(position)?.let {
            viewHolder.bind(it)
        }
    }

    class ViewHolder(private val binding : ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user : Users.User) {
            binding.tvName.text = user.login
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .into(binding.ivUser)
        }
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Users.User>() {
            override fun areItemsTheSame(
                oldItem: Users.User,
                newItem: Users.User
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Users.User,
                newItem: Users.User
            ): Boolean {
                return oldItem.login.equals(newItem.login) && oldItem.avatarUrl.equals(newItem.avatarUrl)
            }
        }
    }
}