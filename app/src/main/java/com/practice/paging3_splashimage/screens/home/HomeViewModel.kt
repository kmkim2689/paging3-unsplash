package com.practice.paging3_splashimage.screens.home

import androidx.lifecycle.ViewModel
import com.practice.paging3_splashimage.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: Repository
): ViewModel() {
    val getAllImages = repository.getAllImages()
}