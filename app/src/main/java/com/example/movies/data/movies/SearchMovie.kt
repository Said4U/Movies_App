package com.example.movies.data.movies

import com.example.movies.data.movies.Film

data class SearchMovie(
    val films: List<Film>,
    val keyword: String,
    val pagesCount: Int,
    val searchFilmsCountResult: Int
)