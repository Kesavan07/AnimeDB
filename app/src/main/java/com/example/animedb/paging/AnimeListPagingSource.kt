package com.example.animedb.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.animedb.models.AnimeItem
import com.example.animedb.services.RestAPIService

class AnimeListPagingSource(private val restAPIService: RestAPIService) :
    PagingSource<Int, AnimeItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeItem> {
        return try {
            val currentPage = params.key ?: 1

            val result =
                restAPIService.getAnimeList(page = currentPage).body() ?: return LoadResult.Error(
                    NullPointerException("API Responded with Null")
                )

            val pageFromServer = result.pagination.current_page
            val totalPages = result.pagination.last_visible_page
            val items = result.data

            val nextPage = if (pageFromServer < totalPages) {
                pageFromServer + 1
            } else null

            LoadResult.Page(
                data = items,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AnimeItem>): Int? {
        return state.anchorPosition
    }
}