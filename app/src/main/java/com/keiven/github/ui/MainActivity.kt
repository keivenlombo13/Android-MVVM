package com.keiven.github.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.keiven.github.R
import com.keiven.github.binding.ActivityDataBindingComponent
import com.keiven.github.data.db.entity.Status
import com.keiven.github.data.factory.ViewModelFactory
import com.keiven.github.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    var dataBindingComponent: DataBindingComponent = ActivityDataBindingComponent(this)
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MainAdapter
    val viewModelFactory: ViewModelFactory by inject()
    val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewmodel = mainViewModel
        initAdapter()
        initObserver()
    }

    fun initAdapter() {
        adapter = MainAdapter(dataBindingComponent)
        binding.rvHome.adapter = adapter
    }

    fun initObserver() {
        mainViewModel.getUsers().observe(this, Observer {
            adapter.submitList(it)
        })
        mainViewModel.loadingState().observe(this, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.pbMain.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.pbMain.visibility = View.VISIBLE
                }
                Status.EMPTY -> {
                    binding.pbMain.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.pbMain.visibility = View.GONE
                    Snackbar.make(binding.rlContainer, it.message ?:
                    "Something went wrong, please try again later",
                        Snackbar.LENGTH_LONG).show();
                }
            }
        })
    }
}