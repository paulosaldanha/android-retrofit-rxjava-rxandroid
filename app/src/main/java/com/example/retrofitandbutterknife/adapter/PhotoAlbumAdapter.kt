package com.example.retrofitandbutterknife.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitandbutterknife.R
import com.example.retrofitandbutterknife.model.PhotoAlbum
import com.squareup.picasso.Picasso

class PhotoAlbumAdapter(private val photos: List<PhotoAlbum>, private val context: Context):
    RecyclerView.Adapter<PhotoAlbumAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(
           LayoutInflater.from(context).inflate(
               R.layout.list_photo_album_item,
               parent,
               false
           )
       )
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo=photos.get(position)
        holder.titlePhotoTextView.text=photo.title
        Picasso.get().load(photo.thumbnailUrl).into(holder.imagePhotoView)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var titlePhotoTextView:TextView = itemView.findViewById(R.id.photoTitle)
        var imagePhotoView:ImageView = itemView.findViewById(R.id.photoView)

    }

}