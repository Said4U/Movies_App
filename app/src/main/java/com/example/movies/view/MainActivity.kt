package com.example.movies.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.movies.R
import com.example.movies.view.favorites.FavoritesFragment
import com.example.movies.viewmodel.MoviesActivityViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val moviesActivityViewModel = MoviesActivityViewModel()

    private lateinit var favoritesFragment : FavoritesFragment
    private lateinit var homeFragment : HomeFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var userID : String
    private lateinit var userName : String

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.i("initFragments", "RESULT_OK")
            initFragments()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)


        val intent = Intent()
        intent.setClass(this, LaunchActivity::class.java)
        startActivityForResult(intent, RESULT_OK)

        val idPref = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        userID = idPref.getString("userId", "").toString()
        userName = idPref.getString("name", "").toString()

        Log.i("initFragments", "onCreate")

        if (userID == ""){
            openRegistrationScreen()
        }else{
            Log.i("initFragments", "else")
            initFragments()
        }
    }

    private fun initFragments(){
        Log.i("initFragments", "initFragments")
        val bundle = Bundle()
        bundle.putString("id", userID)
        bundle.putString("name", userName)
        favoritesFragment = FavoritesFragment()
        homeFragment = HomeFragment.getNewInstance(bundle)
        profileFragment = ProfileFragment.getNewInstance(bundle)

        setCurrentFragment(homeFragment)

        setBottomNavigationView()
    }


    private fun setBottomNavigationView(){
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

    private fun getGenresPreferences(userId: String, userName: String) {
        moviesActivityViewModel.getGenresPreferences(userId)
        moviesActivityViewModel.apply {
            genresPreferences.observe(this@MainActivity) {
                if (it.size == 1) {
                    startForResult.launch(Intent(this@MainActivity, GenresPreferencesActivity::class.java))
                }
            }
        }
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

            getGenresPreferences(user.uid, user.displayName.toString())

//            val profileUpdates = UserProfileChangeRequest.Builder()
//                .setDisplayName("Шихсаид Шихсаидов").build()
//
//            user.updateProfile(profileUpdates).addOnCompleteListener {
//                Log.i("name", user.displayName.toString())
//            }


        }
    }
}