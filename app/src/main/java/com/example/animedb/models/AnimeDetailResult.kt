package com.example.animedb.models

data class AnimeDetailResult(
    val data: AnimeData
)

data class AnimeData(
    val title: String,
    val title_english: String?,
    val synopsis: String,
    val genres: List<GenreItem>,
    val episodes: Int,
    val score: Double,
    val images: AnimeImageData,
    val trailer: Trailer
)

data class GenreItem (
    val name: String,
)

data class Trailer (
    val url: String,
    val embed_url: String,
)