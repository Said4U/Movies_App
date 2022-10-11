package com.example.films.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val email: String, val digitLst: List<String>)
