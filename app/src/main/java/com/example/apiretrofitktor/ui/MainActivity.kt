package com.example.apiretrofitktor.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiretrofitktor.PokemonViewModelFactory
import com.example.apiretrofitktor.R
import com.example.apiretrofitktor.data.ServiceLocatorAPI
import com.example.apiretrofitktor.base.LoadingDialog
import com.example.apiretrofitktor.data.PokemonRepository
import com.example.apiretrofitktor.data.local.LocalDataSource
import com.example.apiretrofitktor.data.local.room.RoomBuilder
import com.example.apiretrofitktor.data.remote.NetworkDataSource
import com.example.apiretrofitktor.data.remote.model.Pokemon
import com.example.apiretrofitktor.data.remote.model.toPokemonItem
import com.example.apiretrofitktor.data.remote.retrofit.RetrofitService
import com.example.apiretrofitktor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: PokemonViewModel
    private lateinit var mAdapter: PokemonAdapter
    private var offset = 0
    private var limit = 30
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

        val retrofitService = RetrofitService(ServiceLocatorAPI.getApiService())
        val ktorClient = ServiceLocatorAPI.initKtorClient()
        val networkDataSource = NetworkDataSource(ktorClient)
        val localDataSource = LocalDataSource(RoomBuilder.getInstance(this).pokemonLocalDAO())
        val pokemonRepository = PokemonRepository(networkDataSource, localDataSource)
        viewModel =
            ViewModelProvider(
                this,
                PokemonViewModelFactory(pokemonRepository),
            )[PokemonViewModel::class.java]
    }
    private fun initView() {
        mAdapter = PokemonAdapter(this)
        viewModel.getPokemon(limit, offset)
        binding.RecyclerView.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = (layoutManager as GridLayoutManager).childCount
                    val totalItemCount = (layoutManager as GridLayoutManager).itemCount
                    val firstVisibleItemPosition = (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

                    if (!viewModel.isLoadMore.value!! && (visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0) {
                        // Load more items
                        viewModel.isLoadMore.value = true
                        offset += limit
                        if(offset < 1320){
                            viewModel.getPokemon(limit,offset)
                        }
                    }
                }
            })
        }
    }

    private fun initListener() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun initObserveData() {
        viewModel.listPokemon.observe(this){ list ->
            mAdapter.addMoreItem(list.map { 
                it.toPokemonItem()
            })
        }
        viewModel.isLoading.observe(this) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.hide()
            }
        }
    }
}
