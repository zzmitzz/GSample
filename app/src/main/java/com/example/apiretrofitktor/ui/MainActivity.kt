package com.example.apiretrofitktor.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiretrofitktor.PokemonViewModelFactory
import com.example.apiretrofitktor.R
import com.example.apiretrofitktor.base.LoadingDialog
import com.example.apiretrofitktor.data.PokemonRepository
import com.example.apiretrofitktor.data.remote.retrofit.ApiServicePokemon
import com.example.apiretrofitktor.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var viewModel: PokemonViewModel

    @Inject
    lateinit var retrofit: ApiServicePokemon

    @Inject
    lateinit var pokemonRepository: PokemonRepository
    private lateinit var mAdapter: PokemonAdapter
    private var offset = 0
    private var limit = 7
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadingDialog = LoadingDialog(this)
        initViewModel()
        initListener()
        initObserveData()
        initView()
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                PokemonViewModelFactory(pokemonRepository),
            )[PokemonViewModel::class.java]
    }

    private fun initView() {
        mAdapter = PokemonAdapter(this)
        lifecycleScope.launch {
            if (isConnected()) {
                viewModel.getRemotePokemon(limit, offset)
            } else {
                viewModel.getOfflinePokemon()
            }
            binding.RecyclerView.apply {
                adapter = mAdapter
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(this@MainActivity, 2)
                addOnScrollListener(
                    object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(
                            recyclerView: RecyclerView,
                            dx: Int,
                            dy: Int,
                        ) {
                            super.onScrolled(recyclerView, dx, dy)
                            val visibleItemCount = (layoutManager as GridLayoutManager).childCount
                            val totalItemCount = (layoutManager as GridLayoutManager).itemCount
                            val firstVisibleItemPosition =
                                (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

                            if (!viewModel.isLoadMore.value!! &&
                                (visibleItemCount + firstVisibleItemPosition >= totalItemCount) &&
                                firstVisibleItemPosition >= 0
                            ) {
                                // Load more items
                                viewModel.isLoadMore.value = true
                                offset += limit
                                if (offset < 1320) {
                                    try {
                                        viewModel.getRemotePokemon(limit, offset)
                                    } catch (e: Exception) {
                                        Log.d("Error", "Error: $e")
                                        Toast
                                            .makeText(
                                                this@MainActivity,
                                                "No connection",
                                                Toast.LENGTH_SHORT,
                                            ).show()
                                    }
                                }
                            }
                        }
                    },
                )
            }
        }
    }

    private fun initListener() {
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.nukeDb.setOnClickListener {
            viewModel.NukeDB()
        }
    }

    private fun initObserveData() {
        viewModel.listPokemon.observe(this) { list ->
            mAdapter.addMoreItem(list)
        }
        viewModel.isLoading.observe(this) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.hide()
            }
        }
    }

    suspend fun isConnected(): Boolean =
        withContext(Dispatchers.IO) {
            val connectivityManager =
                application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
}
