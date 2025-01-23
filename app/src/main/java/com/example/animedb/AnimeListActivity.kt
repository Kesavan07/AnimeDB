package com.example.animedb

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.animedb.models.AnimeItem
import com.example.animedb.ui.theme.AnimeDBTheme
import com.example.animedb.viewmodels.AnimeViewModel
import kotlin.math.floor

class AnimeListActivity : ComponentActivity() {
    private val animeViewModel by viewModels<AnimeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb(),
            ),
        )
        setContent {
            AnimeDBTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(), containerColor = Color.Black
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp)
                            .padding(top = 24.dp),
                    ) {
                        Text(
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                            ),
                            text = "Top Anime's of All Time",
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        AnimeList(animeViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Composable
fun AnimeList(
    animeViewModel: AnimeViewModel
) {
    val lazyPagingAnimeItems = animeViewModel.pagedAnimeList.collectAsLazyPagingItems()

    when (val refreshState = lazyPagingAnimeItems.loadState.refresh) {
        is LoadState.Loading -> {
            // Loading Spinner
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(alignment = androidx.compose.ui.Alignment.Center)
                )
            }
        }

        is LoadState.Error -> {
            // Error Message
            Text(text = "Error: ${refreshState.error.message}")
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    lazyPagingAnimeItems.itemCount,
                    key = lazyPagingAnimeItems.itemKey { it.mal_id }) { index ->
                    val item = lazyPagingAnimeItems[index]
                    if (item != null) {
                        AnimeItemRow(item)
                    }
                }

                // Load More Progress
                if (lazyPagingAnimeItems.loadState.append is LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimeItemRow(item: AnimeItem) {
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(45, 45, 45, 255),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable {
                val intent = Intent(context, AnimeDetailActivity::class.java).apply {
                    putExtra("animeId", item.mal_id)
                }
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, Color(53, 53, 53, 255)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(item.images.jpg.image_url)
                    .crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 120.dp, height = 150.dp)
                    .clip(RoundedCornerShape(6.dp)),
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    item.title_english ?: item.title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Text(
                    style = TextStyle(color = Color.White, fontSize = 16.sp),
                    text = "Total Episodes - ${item.episodes}",
                )
                Text(
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                    ),
                    text = "Rating ${String.format("%.1f", item.score)}",
                )
            }
        }
    }
}
