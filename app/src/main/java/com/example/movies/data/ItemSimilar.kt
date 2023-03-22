package com.example.movies.data

data class ItemSimilar(
    val filmId: Int,
    val nameEn: String,
    val nameOriginal: String,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val relationType: String
)