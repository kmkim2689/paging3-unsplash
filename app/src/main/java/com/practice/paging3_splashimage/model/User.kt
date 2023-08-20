package com.practice.paging3_splashimage.model

import androidx.room.Embedded
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable // kotlinx serialization library can understand that this data class will be used to deserialize api response
data class User(
    @SerialName("links") @Embedded
    val userLinks: UserLinks,
    @SerialName("username")
    val userName: String
)
