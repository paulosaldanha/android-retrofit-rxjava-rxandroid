package com.example.retrofitandbutterknife

import android.content.pm.PackageManager
import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.checkSelfPermission
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.ArrayCompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var retrofit: Retrofit
    lateinit var api: AlbumService
    var myCompositeDisposable: CompositeDisposable = CompositeDisposable()
    var action_busca:Int = 1

    fun btnInit(){
        btnNovo.setOnClickListener(this)
        btnBuscar.setOnClickListener(this)
        btnExcluir.setOnClickListener(this)
        btnSalvar.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        btnInit()

        retrofit = NetworkUtils.getRetrofitInstance("https://jsonplaceholder.typicode.com/")
        api = retrofit.create(AlbumService::class.java)

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

    private fun handleResponse(albumList: List<Album>) {
        Log.e("Response:",albumList.toString())
        txtIdAlbum.setText(albumList[0].id.toString())
        txtTitle.setText(albumList[0].title)
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
            val id: Int = txtIdAlbum.text.toString().toInt()
            Log.e("ID:",id.toString())
            this.myCompositeDisposable?.add(api.selectAlbum(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object: DisposableSingleObserver<Album>() {
                    override fun onSuccess(albumList: Album) {
                        Log.e("Response:",albumList.toString())
                        txtIdAlbum.setText(albumList.id.toString())
                        txtTitle.setText(albumList.title)
                    }

                    override fun onError(e: Throwable) {
                        Log.e("ERROAAAA", e.message)
                        e.printStackTrace()
                    }
                }))


            this.myCompositeDisposable?.add(api.selectAlbums()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object: DisposableSingleObserver<List<Album>>() {
                    override fun onSuccess(albumList: List<Album>) {
                        Log.e("ALBUMS:", "-------------------------------")
                        // txtIdAlbum.setText(albumList.id.toString())
                        // txtTitle.setText(albumList.title)
                        for (album: Album in albumList) {
                            Log.e("ALBUM_INFO", album.id.toString() + " -> " + album.title)
                        }
                        Log.e("ALBUMS:", "-------------------------------")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("ERROAAAA", e.message)
                        e.printStackTrace()
                    }
                }))

//            val request:Call<List<Album>> = api.selectAlbum(id)
//            request.enqueue(object : Callback<List<Album>>{
//                override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
//                   if(response.code() == 200){
//                       val albumResponse = response.body()!!
//                       txtIdAlbum.text = albumResponse.get(0).id.toString()
//                       txtTitle.text = albumResponse.get(0).title
//                   }
//                }
//
//                override fun onFailure(call: Call<List<Album>>, t: Throwable) {
//                    Toast.makeText(applicationContext,"Item with given id was not found",Toast.LENGTH_LONG).show()
//                }
//            })
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
        if(id == null || id == 0){
            id = 0
        }

        if(id > 0){
            //update
        }else{
            //insert
        }

        if(!sucesso){
            if(id > 0){
                Toast.makeText(applicationContext,"Item with id given was not found",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext,"Item was not possible to save",Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(applicationContext,"Item was saved with success",Toast.LENGTH_LONG).show()
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
