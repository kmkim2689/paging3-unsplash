package com.practice.paging3_splashimage.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.practice.paging3_splashimage.data.local.UnsplashDatabase
import com.practice.paging3_splashimage.data.paging.UnsplashRemoteMediator
import com.practice.paging3_splashimage.data.remote.UnsplashApi
import com.practice.paging3_splashimage.model.UnsplashImage
import com.practice.paging3_splashimage.util.Constants.ITEMS_PER_PAGE
import javax.inject.Inject

class Repository @Inject constructor(
    private val unsplashApi: UnsplashApi,
    private val unsplashDatabase: UnsplashDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getAllImages(): kotlinx.coroutines.flow.Flow<PagingData<UnsplashImage>> {
        // single source of truth - database
        val pagingSourceFactory = {
            unsplashDatabase.unsplashImageDao().getAllImages()
        }

        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            remoteMediator = UnsplashRemoteMediator(
                unsplashApi = unsplashApi,
                unsplashDatabase = unsplashDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}