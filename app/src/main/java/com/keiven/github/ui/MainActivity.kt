package com.keiven.github.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.keiven.github.R
import com.keiven.github.binding.ActivityDataBindingComponent
import com.keiven.github.data.db.entity.Status
import com.keiven.github.data.factory.ViewModelFactory
import com.keiven.github.databinding.ActivityMainBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MainAdapter
    val mainViewModel: MainViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initObserver()
        initAction()
    }

    fun initAdapter() {
        adapter = MainAdapter()
        binding.rvHome.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter()
        )
    }

    fun initObserver() {
        val disposable = mainViewModel.getUser().subscribe {
            binding.srlMain.isRefreshing = false
            adapter.submitData(lifecycle, it)
        }
        compositeDisposable.add(disposable)
    }

    fun initAction() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    mainViewModel.setQuery(it)
                    adapter.refresh()
                    binding.rvHome.smoothScrollToPosition(0)
                    binding.searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        binding.srlMain.setOnRefreshListener {
            adapter.refresh()
            binding.rvHome.smoothScrollToPosition(0)
        }

        adapter.addLoadStateListener { loadState ->
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please try again")
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Retry") { _, _ ->
                        adapter.retry()
                    }
                    .show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}