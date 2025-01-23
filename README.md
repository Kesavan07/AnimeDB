# AnimeDB App Overview

Android application built using Android Studio with Kotlin, showcase top Anime List populated using Jikan Anime API and a Detail page for the same

##### Config
* App Architecture - MVVM
* UI - JetPack Compose
* Networking - Retrofit
* Third Party Libs - [Coil](https://coil-kt.github.io/coil/) (for loading images using URL)
* RestAPI - [Jikan Anime API](https://jikan.moe/)
* Android Helper Libraries - [Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)

### Screens

#### List Screen

Top anime list is fetched from Rest API using Retrofit. The network request is initiated using `ViewModel` and provided to the 
composable activity. The data is presented in UI using `LazyColumn` Jetpack component. The subsequent data is loaded when the user
scrolls to the bottom using the android paging library. On tapping the card the user is navigated to the Detail Screen.

#### Detail Screen

In this screen, additional detail of the anime is presented with fields plot/synopsis and genre.
Additional data of the anime is fetched using RestAPI with animeId received from List Screen via Intent.
The Genre list is populated using the `SuggestionChip` component
