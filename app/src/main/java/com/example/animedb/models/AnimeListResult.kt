package com.example.animedb.models

data class AnimeListResult(
    val data: List<AnimeItem>,
    val pagination: ResultPagination
)

data class AnimeItem(
    val mal_id: Int,
    val title: String,
    val title_english: String?,
    val episodes: Int,
    val score: Double,
    val images: AnimeImageData
)

data class AnimeImageData(
    val jpg: AnimeImageJPG
)

data class AnimeImageJPG(
    val image_url: String
)

data class ResultPagination(
    val last_visible_page: Int,
    val current_page: Int
)
