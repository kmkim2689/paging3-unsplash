package com.practice.paging3_splashimage.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practice.paging3_splashimage.util.Constants.UNSPLASH_IMAGE_TABLE
import kotlinx.serialization.Serializable

@Entity(tableName = UNSPLASH_IMAGE_TABLE)
@Serializable // every model class(using api)
data class UnsplashImage(
    @PrimaryKey(autoGenerate = false) // id is already the unique value...
    val id: String, // id for image
    @Embedded
    val urls: Urls, // hot links
    val likes: Int,
    @Embedded
    val user: User
)

/*
@Embedded annotation 사용으로,
unsplash_image_table 데이터베이스 테이블의 필드는 다음과 같다.
id, likes, regular, html, username
 */


