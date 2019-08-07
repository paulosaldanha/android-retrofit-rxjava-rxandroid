package com.example.retrofitandbutterknife.service

import com.example.retrofitandbutterknife.model.Album
import com.example.retrofitandbutterknife.model.PhotoAlbum
import io.reactivex.Single
import retrofit2.http.*

interface AlbumService {
    @GET("albums")
    fun selectAlbums(): Single<List<Album>>

    @GET("albums/{id}")
    fun selectAlbum(@Path("id") id:Int): Single<Album>

    @FormUrlEncoded
    @POST("albums")
    fun insertAlbum(@Field("title") title:String, @Field("userId") userId:Int): Single<Album>

    @FormUrlEncoded
    @PUT("albums/{id}")
    fun updateAlbum(@Path("id") id:Int, @Field("title") title:String, @Field("userId") userId:Int): Single<Album>

    @DELETE("albums/{id}")
    fun deleteAlbum(@Path("id") id:Int): Single<Album>

    //photos of the album
    @GET("photos")
    fun getPhotos(@Query("albumId") id:Int): Single<List<PhotoAlbum>>
}