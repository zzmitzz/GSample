package com.example.apiretrofitktor.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiretrofitktor.R
import com.example.apiretrofitktor.base.LoadingDialog
import com.example.apiretrofitktor.data.PokemonRepository
import com.example.apiretrofitktor.data.ResultOf
import com.example.apiretrofitktor.data.remote.retrofit.ApiServicePokemon
import com.example.apiretrofitktor.databinding.ActivityMainBinding
import com.example.apiretrofitktor.ui.mvi.PokemonIntent
import com.example.apiretrofitktor.ui.mvi.PokemonState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZoneOffset
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    val viewModel : PokemonViewModel by viewModels()
    @Inject
    lateinit var mAdapter: PokemonAdapter
    private var offset = 0
    private var limit = 7
    @Inject
    lateinit var loadingDialog: LoadingDialog
    private var isConnect = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        lifecycleScope.launch { isConnect = isConnected() }
        lifecycleScope.launch {
            viewModel.state.collect{
                render(it)
            }
        }
        initData()
        recyclerViewSetup()
        initEvent()
    }


    private fun initUI(){
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun render(state: PokemonState){
        lifecycleScope.launch {
            if(state.loading){
                loadingDialog.show()
            }else{
                loadingDialog.hide()
            }
            if(state.error != null){
                Toast.makeText(this@MainActivity,"Network error",Toast.LENGTH_SHORT).show()
            }
            if(state.listPokemon.isNotEmpty()){
                mAdapter.addMoreItem(state.listPokemon)
            }
        }
    }

    private fun initData(){
        dispatcherIntent(PokemonIntent.LoadingPokemon(isConnect,offset,limit))
    }
    private fun initEvent(){
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.nukeDb.setOnClickListener {
            viewModel.handleIntent(PokemonIntent.DeleteDb)
        }
    }

    private fun dispatcherIntent(intent: PokemonIntent){
        viewModel.handleIntent(intent)
    }

    private suspend fun isConnected(): Boolean =
        withContext(Dispatchers.IO) {
            val connectivityManager =
                application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.activeNetworkInfo?.isConnected ?: false
        }.also {
            Log.d("Connect", it.toString())
        }

    private fun recyclerViewSetup(){
        lifecycleScope.launch {
            isConnect = isConnected()
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

                            if (!viewModel.isLoadMore &&
                                (visibleItemCount + firstVisibleItemPosition >= totalItemCount) &&
                                firstVisibleItemPosition >= 0
                            ) {
                                // Load more items
                                viewModel.isLoadMore = true
                                offset += limit
                                if (offset < 1320) {
                                    try {
                                        viewModel.handleIntent(PokemonIntent.LoadingPokemon(isConnect,offset,limit))
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
}
