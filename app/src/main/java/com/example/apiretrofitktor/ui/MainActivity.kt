package com.example.apiretrofitktor.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.apiretrofitktor.PokemonViewModelFactory
import com.example.apiretrofitktor.R
import com.example.apiretrofitktor.ServiceLocatorAPI
import com.example.apiretrofitktor.base.LoadingDialog
import com.example.apiretrofitktor.data.model.toPokemonItem
import com.example.apiretrofitktor.data.remote.retrofit.RetrofitService
import com.example.apiretrofitktor.databinding.ActivityMainBinding
import com.example.apiretrofitktor.ui.model.PokemonItem
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import kotlinx.coroutines.launch

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
        viewModel =
            ViewModelProvider(
                this,
                PokemonViewModelFactory(retrofitService),
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
                            viewModel.loadMorePokemon(limit,offset)
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
        viewModel.response.observe(this) {
            mAdapter.setData(it.results.map { it.toPokemonItem() })
        }
        viewModel.cachePokemon.observe(this){
            mAdapter.addMoreItem(it.results.map { it.toPokemonItem() })
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
