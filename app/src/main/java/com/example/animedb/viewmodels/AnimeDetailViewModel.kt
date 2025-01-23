package com.example.animedb.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animedb.models.AnimeDetailState
import com.example.animedb.services.RetrofitClient
import kotlinx.coroutines.launch

class AnimeDetailViewModel: ViewModel() {
    private val _animeDetailState = mutableStateOf<AnimeDetailState>(AnimeDetailState.Loading)
    val animeDetailState: State<AnimeDetailState> = _animeDetailState

    fun fetchAnimeDetail(id: Int) {
        viewModelScope.launch {
            try {
                _animeDetailState.value = AnimeDetailState.Loading
                val result = RetrofitClient.restAPIService.getAnimeDetail(id).body()
                if (result != null) _animeDetailState.value = AnimeDetailState.Success(result.data)
            } catch (e: Exception) {
                e.printStackTrace()
                _animeDetailState.value = AnimeDetailState.Error("API Failed")
            }
        }
    }
}