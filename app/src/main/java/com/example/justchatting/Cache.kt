package com.example.justchatting

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.LruCache

class Cache {
    fun saveBitmap(context: Context, bitmap: Bitmap?){
        context.openFileOutput("profile", Context.MODE_PRIVATE).use { os->
            bitmap?.let { it.compress(Bitmap.CompressFormat.PNG, 100, os) }
        }
    }
    fun loadBitmap(context: Context): Bitmap? {
        val file = context.getFileStreamPath("profile")
        if(file.exists())
            return BitmapFactory.decodeStream(file.inputStream())
        else
            return null
    }
}