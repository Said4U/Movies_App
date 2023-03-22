package com.example.movies.view.favorites

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.view.MoviesDetailActivity
import com.example.movies.view.adapter.MoviesDetailAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import kotlinx.android.synthetic.main.activity_favorites_list.*
import kotlinx.android.synthetic.main.fragment_recommendation.*

class FavoritesListActivity : AppCompatActivity(), MoviesDetailAdapter.ItemClickListener {

    private val moviesActivityViewModel = MoviesActivityViewModel()
    private lateinit var idPref: SharedPreferences
    private lateinit var userID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites_list)

        idPref = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
        userID = idPref.getString("userId", "").toString()

        recyclerViewFavoritesList.layoutManager = GridLayoutManager(this, 2)

        initObservers()
    }

    private fun initObservers(){
        moviesActivityViewModel.getFavoritesFull(userID)

        moviesActivityViewModel.apply {
            moviesDetailList.observe(this@FavoritesListActivity) {
                recyclerViewFavoritesList.adapter = MoviesDetailAdapter(it, this@FavoritesListActivity)
            }
        }
    }

        override fun onItemClick(id: Int) {
        val moviesDetailIntent = Intent(this, MoviesDetailActivity::class.java)
        moviesDetailIntent.putExtra("userId", userID)
        moviesDetailIntent.putExtra("movieId", id)
        startActivity(moviesDetailIntent)
    }
}