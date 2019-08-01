package com.example.retrofitandbutterknife

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

interface AlbumService {
    @GET("albums")
    fun selectAlbums(): Single<List<Album>>

    @GET("albums/{id}")
    fun selectAlbum(@Path("id") id:Int): Single<Album>

    @FormUrlEncoded
    @POST("albums")
    fun insertAlbum(@Field("title") title:String): Observable<Album>

    @FormUrlEncoded
    @PUT("albums/{id}")
    fun updateAlbum(@Path("id") id:Int, @Field("title") title:String): Observable<Album>

    @DELETE("albums/{id}")
    fun deleteAlbum(@Path("id") id:Int): Observable<Album>
}