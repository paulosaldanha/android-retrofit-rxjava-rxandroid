package com.example.retrofitandbutterknife.base

import androidx.lifecycle.ViewModel
import com.example.retrofitandbutterknife.DaggerViewModelInjector
import com.example.retrofitandbutterknife.ViewModelInjector
import com.example.retrofitandbutterknife.utils.NetworkUtils
import com.example.retrofitandbutterknife.viewModel.AlbumFotoViewModel


abstract class BaseViewModel: ViewModel() {
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkUtils(NetworkUtils)
        .build()

    init {
        inject()
    }

    private fun inject(){
        when(this){
            is AlbumFotoViewModel -> injector.inject(this)
        }
    }
}