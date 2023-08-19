package com.practice.paging3_splashimage.model

import kotlinx.serialization.Serializable

@Serializable // kotlinx serialization library can understand that this data class will be used to deserialize api response
data class Urls(
    val regular: String
)
