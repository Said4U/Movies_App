package com.example.movies.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val email: String, val digitLst: List<String>)
