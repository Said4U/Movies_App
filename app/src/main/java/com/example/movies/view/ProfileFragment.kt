package com.example.movies.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColor
import com.example.movies.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.card_view_design.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idPref = requireActivity().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)

        userName.text = idPref.getString("name", "").toString()

        signOutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val idPrefEdit = requireActivity().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE).edit()
            idPrefEdit.remove("userId")
            idPrefEdit?.apply()

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        changePreferencesBtn.setOnClickListener{
            val intent = Intent(context, GenresPreferencesActivity::class.java)
            startActivity(intent)
        }
    }

}