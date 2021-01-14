package com.example.justchatting

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter


@BindingAdapter("android:imageBitmap")
fun setImageBitmap(imageView: ImageView, bitmap: Bitmap?) {
    if(bitmap != null)
        imageView.setImageBitmap(bitmap)
    else
        imageView.setImageResource(R.drawable.person)
}
