package com.example.movies.data.movies

data class MoviesData(
    val items: List<Item>,
    val total: Int,
    val totalPages: Int?
)