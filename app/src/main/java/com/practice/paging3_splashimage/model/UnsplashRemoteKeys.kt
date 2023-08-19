package com.practice.paging3_splashimage.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practice.paging3_splashimage.util.Constants.UNSPLASH_REMOTE_KEYS_TABLE

@Entity(tableName = UNSPLASH_REMOTE_KEYS_TABLE)
data class UnsplashRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    // the first/last page doesn't have prevPage/nextPage => nullable
    val prevPage: Int?,
    val nextPage: Int?
)
