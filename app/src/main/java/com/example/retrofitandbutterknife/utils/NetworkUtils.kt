package com.example.retrofitandbutterknife.utils

import com.example.retrofitandbutterknife.service.AlbumService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@Suppress("unused")
object NetworkUtils {
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideAlbumApi(retrofit: Retrofit):AlbumService {
        return retrofit.create(AlbumService::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal  fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}
