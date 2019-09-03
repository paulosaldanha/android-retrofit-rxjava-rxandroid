package com.example.retrofitandbutterknife

import com.example.retrofitandbutterknife.utils.NetworkUtils
import com.example.retrofitandbutterknife.viewModel.AlbumFotoViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkUtils::class)])
interface ViewModelInjector {
    fun inject(albumFotoViewModel: AlbumFotoViewModel)

    @Component.Builder
    interface Builder {
        fun build():ViewModelInjector

        fun  networkUtils(networkUtils: NetworkUtils):Builder
    }
}