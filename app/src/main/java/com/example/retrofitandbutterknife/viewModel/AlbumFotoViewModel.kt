package com.example.retrofitandbutterknife.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.retrofitandbutterknife.base.BaseViewModel
import com.example.retrofitandbutterknife.service.AlbumService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AlbumFotoViewModel:BaseViewModel() {
    @Inject
    lateinit var albumService: AlbumService

    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    init{
        loadAlbumFoto()
    }

    private fun loadAlbumFoto(){
        subscription = albumService.selectAlbum(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveAlbumStart() }
            //.doOnTerminate { onRetrieveAlbumFinish() }
            .subscribe(
                { onRetrieveAlbumSuccess() },
                { onRetrieveAlbumError() }
            )
    }

    private fun onRetrieveAlbumStart(){
        loadingVisibility.value = View.VISIBLE
    }

    private fun onRetrieveAlbumFinish(){
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveAlbumSuccess(){

    }

    private fun onRetrieveAlbumError(){

    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}