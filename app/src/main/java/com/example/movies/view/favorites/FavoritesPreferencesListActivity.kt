package com.example.movies.view.favorites

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.view.MoviesDetailActivity
import com.example.movies.view.adapter.FavoritesPreferencesAdapter
import com.example.movies.view.adapter.FullFavoritesPreferencesAdapter
import com.example.movies.view.adapter.MoviesDetailAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import kotlinx.android.synthetic.main.activity_favorites_list.*
import kotlinx.android.synthetic.main.activity_favorites_preferences_list.*

class FavoritesPreferencesListActivity : AppCompatActivity(), FullFavoritesPreferencesAdapter.ItemClickListener {

    private val moviesActivityViewModel = MoviesActivityViewModel()
    private lateinit var idPref: SharedPreferences
    private lateinit var userID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites_preferences_list)

        idPref = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
        userID = idPref.getString("userId", "").toString()

        recyclerViewFavoritesPrefList.layoutManager = GridLayoutManager(this, 2)

        initObservers()
    }

    private fun initObservers(){
        moviesActivityViewModel.getFavoritesID(userID)


        moviesActivityViewModel.apply {
            moviesIdList.observe(this@FavoritesPreferencesListActivity) { movieIdList ->
                val maxLength = if (movieIdList.size >= 4) 4
                else movieIdList.size
                moviesActivityViewModel.getSimilarList(movieIdList.shuffled().subList(0, maxLength))

                moviesSimilar.observe(this@FavoritesPreferencesListActivity) {
                    recyclerViewFavoritesPrefList.adapter = FullFavoritesPreferencesAdapter(it, this@FavoritesPreferencesListActivity)
                }
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