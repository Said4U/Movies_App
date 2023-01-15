package com.example.movies.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.movies.R
import com.example.movies.viewmodel.MainActivityViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel = MainActivityViewModel()

    private lateinit var favoritesFragment : FavoritesFragment
    private lateinit var homeFragment : HomeFragment
    private lateinit var profileFragment: ProfileFragment

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent()
        intent.setClass(this, LaunchActivity::class.java)
        startActivityForResult(intent, RESULT_OK);

        val idPref = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        val userID = idPref.getString("userId", "")
        val userName = idPref.getString("name", "")

        if (userID == ""){
            openRegistrationScreen()
        }else{
            val bundle = Bundle()
            bundle.putString("id", userID)
            bundle.putString("name", userName)
            favoritesFragment = FavoritesFragment.getNewInstance(bundle)
            homeFragment = HomeFragment.getNewInstance(bundle)
            profileFragment = ProfileFragment.getNewInstance(bundle)

            setCurrentFragment(homeFragment)
        }




        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.favorites -> setCurrentFragment(favoritesFragment)
                R.id.profile -> setCurrentFragment(profileFragment)

            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    private fun openRegistrationScreen(){

        // Choose authentication providers
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser!!





            val idPrefEdit = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit()
            idPrefEdit.putString("userId", user.uid)
            idPrefEdit.putString("name", user.displayName)
            idPrefEdit.apply()

            val bundle = Bundle()
            bundle.putString("id", user.uid)
            bundle.putString("name", user.displayName)

//            val profileUpdates = UserProfileChangeRequest.Builder()
//                .setDisplayName("Шихсаид Шихсаидов").build()
//
//            user.updateProfile(profileUpdates).addOnCompleteListener {
//                Log.i("name", user.displayName.toString())
//            }

            favoritesFragment = FavoritesFragment.getNewInstance(bundle)
            homeFragment = HomeFragment.getNewInstance(bundle)
            profileFragment = ProfileFragment.getNewInstance(bundle)

            setCurrentFragment(homeFragment)

            user.let {
                mainActivityViewModel.writeNewUser(it.uid, it.email.toString())
                mainActivityViewModel.getUser(it.uid)
            }
        }
    }
}