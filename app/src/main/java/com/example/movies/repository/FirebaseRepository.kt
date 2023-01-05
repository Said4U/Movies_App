package com.example.movies.repository

import android.util.Log
import com.example.movies.data.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRepository {

    private var database = Firebase.database.reference
//    private var mapId = mapOf<String, String>()

    // запись пользователя  в бд
    fun writeNewUser(userId: String, email: String) {
        val lst = listOf("1", "2")
        val user = User(email, lst)
        database.child("users").child(userId).setValue(user)
    }

    // извлечение данных пользователя из бд
    fun getUser(userId: String) {
        database.child("users").child(userId).get().addOnSuccessListener {
            val lst = it.value as Map<*, *>
            Log.i("firebase", "Got value ${lst["digitLst"]}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun writeFavorites(userId: String, movieId: String) {
        database.child("favorites").child(userId).child(movieId).setValue(movieId)
    }

    fun getFavorites(userId: String) =
        database.child("favorites").child(userId).get().addOnSuccessListener {
        }.addOnFailureListener {
            Log.e("favorites", "Error getting data", it)
        }



}