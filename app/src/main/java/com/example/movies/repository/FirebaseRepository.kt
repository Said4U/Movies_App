package com.example.movies.repository

import android.util.Log
import com.example.movies.data.User
import com.google.firebase.database.DatabaseReference

class FirebaseRepository {

    private lateinit var database: DatabaseReference

    // запись пользователя  в бд
    fun writeNewUser(userId: String, email: String) {
        val lst = listOf("1", "2")
        val user = User(email, lst)
        database.child("users").child(userId).setValue(user)
    }

    // извлечение данных пользователя изз бд
    fun getUser(userId: String) {
        database.child("users").child(userId).get().addOnSuccessListener {
            val lst = it.value as Map<*, *>
            Log.i("firebase", "Got value ${lst["digitLst"]}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }
}