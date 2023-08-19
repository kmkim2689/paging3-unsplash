package com.practice.paging3_splashimage.data.remote

import com.practice.paging3_splashimage.model.UnsplashImage
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {
    // for paginating api response with paging3 library
    @Headers("Authorization: Client-ID d-b0Xq23J1BmCAVp0dFh5-tTd6cI6B0fgjpPi7nW5P0")
    @GET("/photos")
    suspend fun getAllImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<UnsplashImage>

    // searching a specific image
    @Headers("Authorization: Client-ID d-b0Xq23J1BmCAVp0dFh5-tTd6cI6B0fgjpPi7nW5P0")
    @GET("/search/photos")
    suspend fun searchImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<UnsplashImage>
}