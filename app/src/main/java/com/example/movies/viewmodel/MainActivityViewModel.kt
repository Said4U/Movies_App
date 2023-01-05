package com.example.movies.viewmodel

import com.example.movies.repository.FirebaseRepository

class MainActivityViewModel {

    private val firebaseRepository = FirebaseRepository()

    fun writeNewUser(userId: String, email: String) {
        firebaseRepository.writeNewUser(userId, email)
    }

    fun getUser(userId: String) {
        firebaseRepository.getUser(userId)
    }


}