package com.example.movies.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movies.R
import com.example.movies.view.adapter.CustomAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.activity_movies_detail.*

class MoviesDetailActivity : AppCompatActivity() {

    private val moviesActivityViewModel = MoviesActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_detail)

        initObservers()
        val id = intent.extras?.getInt("id")
        moviesActivityViewModel.getMoviesDetail(id)
    }

    private fun initObservers() {
        moviesActivityViewModel.apply {
            movies.observe(this@MoviesDetailActivity) {
                singleMovieName.text = moviesDetail.value?.nameRu ?: moviesDetail.value?.nameEn
            }
        }
    }
}