package com.example.movies.data

data class ItemAward(
    val imageUrl: String,
    val name: String,
    val nominationName: String,
    val persons: List<PersonAward>,
    val win: Boolean,
    val year: Int
)