package com.example.retrofitandbutterknife

import android.content.pm.PackageManager
import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitandbutterknife.adapter.PhotoAlbumAdapter
import com.example.retrofitandbutterknife.model.Album
import com.example.retrofitandbutterknife.model.PhotoAlbum
import com.example.retrofitandbutterknife.service.AlbumService
import com.example.retrofitandbutterknife.utils.NetworkUtils
import com.example.retrofitandbutterknife.viewModel.AlbumFotoViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Retrofit

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //private lateinit var binding: Retrofit
    lateinit var api: AlbumFotoViewModel

    var myCompositeDisposable: CompositeDisposable = CompositeDisposable()
    // lateinit var recyclerView:RecyclerView
    lateinit var adapter: PhotoAlbumAdapter
    var photoList = ArrayList<PhotoAlbum>()
    var action_busca:Int = 1

    fun btnInit(){
        btnNovo.setOnClickListener(this)
        btnBuscar.setOnClickListener(this)
        btnExcluir.setOnClickListener(this)
        btnSalvar.setOnClickListener(this)
    }

    fun clear(){
        var size:Int = photoList.size
        photoList.clear()
        list_photos_album.adapter!!.notifyItemRangeChanged(0,size)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //
        // recyclerView = findViewById(R.id.list_photos_album)
        list_photos_album.adapter= PhotoAlbumAdapter(photoList, this)
        list_photos_album.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)

        btnInit()

        //retrofit = NetworkUtils.getRetrofitInstance("https://jsonplaceholder.typicode.com/")
        //api = retrofit.create(AlbumService::class.java)
        api = ViewModelProviders.of(this,ViewModelProvider.AndroidViewModelFactory(this.application)).get(AlbumFotoViewModel::class.java)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnNovo -> {
                btnNovoOnClick()
            }
            R.id.btnBuscar -> {
                btnBuscarOnClick()
            }
            R.id.btnExcluir -> {
                btnExcluirOnClick()
            }
            R.id.btnSalvar -> {
                btnSalvarOnClick()
            }
        }
    }

    private fun buscaFotos(albumId:Int){
            this.myCompositeDisposable.add(api.albumService.getPhotos(albumId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object: DisposableSingleObserver<List<PhotoAlbum>>() {
                    override fun onSuccess(albumPhotoList: List<PhotoAlbum>) {
                        loadingText.visibility = View.GONE
                        photoList.addAll(albumPhotoList)
                        list_photos_album.adapter!!.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {
                        loadingText.visibility = View.GONE
                        e.printStackTrace()
                    }
                }))
    }

    /*
     * BTNS
     */
    fun btnNovoOnClick():Void? {
        txtIdAlbum.setText("0")
        txtTitle.setText("")
        return null
    }

    fun btnBuscarOnClick():Void? {
        if(checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET),action_busca)
        }else {
            loadingText.visibility = View.VISIBLE
            val id: Int = txtIdAlbum.text.toString().toInt()
            Log.e("ID:",id.toString())
            this.myCompositeDisposable.add(api.albumService.selectAlbum(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object: DisposableSingleObserver<Album>() {
                    override fun onSuccess(albumList: Album) {
                        Log.e("Response:",albumList.toString())
                        txtIdAlbum.setText(albumList.id.toString())
                        txtTitle.setText(albumList.title)
                        buscaFotos(id)
                    }

                    override fun onError(e: Throwable) {
                        Log.e("ERROAAAA", e.message)
                        e.printStackTrace()
                    }
                }))

        }
        return null
    }

    fun btnExcluirOnClick():Void? {
        if(checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET),action_busca)
        }else {
            val id: Int = txtIdAlbum.text.toString().toInt()

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure to delete this item?")
            builder.setPositiveButton("Yes") { dialog, which ->
                this.myCompositeDisposable?.add(api.albumService.deleteAlbum(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object: DisposableSingleObserver<Album>() {
                        override fun onSuccess(albumList: Album) {
                            Log.e("Response:",albumList.toString())
                            txtIdAlbum.setText("")
                            txtTitle.setText("")
                            clear()
                        }

                        override fun onError(e: Throwable) {
                            Log.e("ERROAAAA", e.message)
                            e.printStackTrace()
                        }
                    }))

                //                val request:Call<Album> = api.deleteAlbum(id)
//                request.enqueue(object : Callback<List<Album>> {
//                    override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
//                        if(response.code() == 200){
//                            Toast.makeText(applicationContext, "Deleted with success!", Toast.LENGTH_LONG).show()
//                            txtIdAlbum.text = "0"
//                            txtTitle.text = ""
//                        }
//                    }
//
//                    override fun onFailure(call: Call<List<Album>>, t: Throwable) {
//                        Toast.makeText(applicationContext, "Item was not found", Toast.LENGTH_LONG).show()
//                    }
//                })


            }
            builder.setNegativeButton("No"){dialog, which ->

            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        return null
    }

    fun btnSalvarOnClick():Void? {
        val sucesso = true
        var id:Int? = txtIdAlbum.text.toString().toIntOrNull()
        var title:String = txtTitle.text.toString()
        if(id == null || id == 0){
            id = 0
        }

        if(id > 0){
            //update
            this.myCompositeDisposable?.add(api.albumService.updateAlbum(id,title,1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object: DisposableSingleObserver<Album>() {
                    override fun onSuccess(albumList: Album) {
                        Toast.makeText(applicationContext,"Item was saved with success",Toast.LENGTH_LONG).show()
                        Log.e("Response:",albumList.toString())
                        txtIdAlbum.setText("")
                        txtTitle.setText("")
                        clear()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("ERROAAAA", e.message)
                        e.printStackTrace()
                    }
                }))
        }else{
            //insert
            this.myCompositeDisposable?.add(api.albumService.insertAlbum(title,1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object: DisposableSingleObserver<Album>() {
                    override fun onSuccess(albumList: Album) {
                        Toast.makeText(applicationContext,"Item was saved with success",Toast.LENGTH_LONG).show()
                        Log.e("Response:",albumList.toString())
                        txtIdAlbum.setText("")
                        txtTitle.setText("")
                        clear()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("ERROAAAA", e.message)
                        e.printStackTrace()
                    }
                }))
        }

        return null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
