package com.example.films

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.films.data.User
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

//    private val signInLauncher = registerForActivityResult(
//        FirebaseAuthUIActivityResultContract()
//    ) { res ->
//        this.onSignInResult(res)
//    }

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("Debug", "Main")

        val moviesIntent = Intent(this, MoviesActivity::class.java)
        startActivity(moviesIntent)

//        database = Firebase.database.reference
//
//        // Choose authentication providers
//        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
//
//        // Create and launch sign-in intent
//        val signInIntent = AuthUI.getInstance()
//            .createSignInIntentBuilder()
//            .setAvailableProviders(providers)
//            .build()
//        signInLauncher.launch(signInIntent)
    }

//    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
//        val response = result.idpResponse
//        if (result.resultCode == RESULT_OK) {
//            // Successfully signed in
//            val user = FirebaseAuth.getInstance().currentUser
//            user?.let {
//                writeNewUser(it.uid, it.email.toString())
//                getUser(it.uid)
//            }
//            val moviesIntent = Intent(this, MoviesActivity::class.java)
//            startActivity(moviesIntent)
//        }
//    }
//
//    // запись пользователя  в бд
//    private fun writeNewUser(userId: String, email: String) {
//        val lst = listOf("1", "2")
//        val user = User(email, lst)
//        database.child("users").child(userId).setValue(user)
//    }
//
//    // извлечение данных пользователя изз бд
//    private fun getUser(userId: String) {
//        database.child("users").child(userId).get().addOnSuccessListener {
//            val lst = it.value as Map<*, *>
//            Log.i("firebase", "Got value ${lst["digitLst"]}")
//        }.addOnFailureListener{
//            Log.e("firebase", "Error getting data", it)
//        }
//    }


}