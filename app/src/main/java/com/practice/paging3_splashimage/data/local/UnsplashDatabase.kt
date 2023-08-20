package com.practice.paging3_splashimage.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practice.paging3_splashimage.model.UnsplashImage
import com.practice.paging3_splashimage.model.UnsplashRemoteKeys

@Database(entities = [UnsplashImage::class, UnsplashRemoteKeys::class], version = 1, exportSchema = false)
abstract class UnsplashDatabase: RoomDatabase() {
    abstract fun unsplashImageDao(): UnsplashImageDao
    abstract fun unsplashRemoteKeysDao(): UnsplashRemoteKeysDao

}