package com.example.animedb.services

import com.example.animedb.models.AnimeDetailResult
import com.example.animedb.models.AnimeListResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestAPIService {
    @GET("top/anime")
    suspend fun getAnimeList(@Query("page") page: Int): Response<AnimeListResult>

    @GET("anime/{id}")
    suspend fun getAnimeDetail(@Path("id") id: Int): Response<AnimeDetailResult>
}