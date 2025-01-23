package com.example.animedb.models

sealed class AnimeDetailState {
    object Loading: AnimeDetailState()
    data class Success(val data: AnimeData): AnimeDetailState()
    data class Error(val message: String): AnimeDetailState()
}