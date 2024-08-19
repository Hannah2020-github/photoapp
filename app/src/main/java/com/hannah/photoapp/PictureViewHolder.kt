package com.hannah.photoapp

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PictureViewHolder(val pictureView: View): RecyclerView.ViewHolder(pictureView) {
    private val imageView: ImageView = this.pictureView.findViewById(R.id.image_view)

    fun setImageView(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }
}