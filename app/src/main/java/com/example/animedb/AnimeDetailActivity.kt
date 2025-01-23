package com.example.animedb

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.animedb.models.AnimeDetailState
import com.example.animedb.ui.theme.AnimeDBTheme
import com.example.animedb.viewmodels.AnimeDetailViewModel

class AnimeDetailActivity : ComponentActivity() {
    private val animeDetailViewModel by viewModels<AnimeDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animeId = intent.getIntExtra("animeId", 0)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb(),
            ),
        )

        setContent {
            AnimeDBTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Black,
                ) { innerPadding ->
                    AnimeDetail(innerPadding, animeId, animeDetailViewModel)
                }
            }
        }
    }
}

@Composable
fun AnimeDetail(innerPadding: PaddingValues, animeId: Int, viewModel: AnimeDetailViewModel) {
    LaunchedEffect(animeId) {
        viewModel.fetchAnimeDetail(animeId)
    }
    val state by viewModel.animeDetailState

    when (state) {
        is AnimeDetailState.Loading -> CircularProgressIndicator()
        is AnimeDetailState.Success -> {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val data = (state as AnimeDetailState.Success).data
                Text(
                    data.title_english ?: data.title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data.images.jpg.image_url)
                        .crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(6.dp)),
                )
                Text("Genre",style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                ),)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    data.genres.forEach { chipLabel ->
                        SuggestionChip(
                            onClick = { Log.d("Suggestion chip", "hello world") },
                            label = {
                                Text(
                                    chipLabel.name, style = TextStyle(
                                        color = Color.White,
                                        fontSize = 12.sp,
                                    )
                                )
                            }
                        )
                    }
                }
                Text("Plot/Synopsis", style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                ),)
                Text(
                    data.synopsis,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                    ),
                )
                Text(
                    style = TextStyle(color = Color.White, fontSize = 16.sp),
                    text = "Total Episodes - ${data.episodes}",
                )
                Text(
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                    ),
                    text = "Rating ${String.format("%.1f", data.score)}",
                )
            }
        }

        is AnimeDetailState.Error -> {
            val error = (state as AnimeDetailState.Error).message
            Text(error)
        }
    }

}