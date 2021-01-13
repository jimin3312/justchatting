package com.example.justchatting

import android.graphics.Bitmap
import android.util.LruCache

class Cache {
    private lateinit var memoryCache: LruCache<String, Bitmap>
    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    }

    fun saveBitmap(key: String, bitmap: Bitmap){
        memoryCache.put(key, bitmap)
    }
    fun loadBitmap(key: String): Bitmap? {
        return memoryCache.get(key)
    }
}