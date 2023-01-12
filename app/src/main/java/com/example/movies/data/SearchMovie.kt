package com.example.movies.data

data class SearchMovie(
    val films: List<Film>,
    val keyword: String,
    val pagesCount: Int,
    val searchFilmsCountResult: Int
)