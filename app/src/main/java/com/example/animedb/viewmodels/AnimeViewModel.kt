package com.example.animedb.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.animedb.models.AnimeItem
import com.example.animedb.paging.AnimeListPagingSource
import com.example.animedb.services.RetrofitClient
import kotlinx.coroutines.flow.Flow

class AnimeViewModel : ViewModel() {

    val pagedAnimeList: Flow<PagingData<AnimeItem>> = Pager(config = PagingConfig(
        pageSize = 25, enablePlaceholders = false
    ), pagingSourceFactory = {
        AnimeListPagingSource(RetrofitClient.restAPIService)
    }).flow.cachedIn(viewModelScope)
}