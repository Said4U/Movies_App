package com.example.movies.view

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.movies.R
import com.example.movies.viewmodel.MoviesActivityViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movies_detail.*

class MoviesDetailActivity : AppCompatActivity() {

    private val moviesActivityViewModel = MoviesActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_detail)

        initObservers()
        val userId = intent.extras?.getString("userId")
        val movieId = intent.extras?.getInt("movieId")
        moviesActivityViewModel.getMoviesDetail(movieId!!.toInt())

        add_favorites_btn.setOnClickListener {
            moviesActivityViewModel.writeFavorites(userId!!, movieId.toString())
        }
    }

    private fun initObservers() {
        moviesActivityViewModel.apply {
            moviesDetail.observe(this@MoviesDetailActivity) {
                Log.i("Debug", "Присваиваем")
                Picasso.get().load(it.posterUrl).resize(1500, 1600).into(imageViewDetail)
                singleMovieNameRus.text = it?.nameRu?: it.nameEn?: it.nameOriginal
                singleMovieNameEn.text = it.nameEn?: it.nameOriginal
                slogan.text = it.slogan?: ""
                val ratingDigit = it.ratingKinopoisk
                if (ratingDigit > 6.9) {
                    rating.setBackgroundColor(Color.parseColor("#008000"))
                }
                rating.text = ratingDigit.toString()

                if (slogan.text == ""){
                    slogan.visibility = View.GONE
                }

            }
        }
    }


}