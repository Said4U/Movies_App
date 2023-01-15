package com.example.movies.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.movies.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userName.text = requireArguments().getString("name").toString()

        signOutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val idPrefEdit = requireActivity().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE).edit()
            idPrefEdit.remove("userId")
            idPrefEdit?.apply()

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)

        }
    }

    companion object{
        fun getNewInstance(name: Bundle?): ProfileFragment {
            val profileFragment = ProfileFragment()
            profileFragment.arguments = name
            return profileFragment
        }
    }


}