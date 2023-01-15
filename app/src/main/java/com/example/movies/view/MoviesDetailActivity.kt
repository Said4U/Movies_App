package com.example.movies.view

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.example.movies.R
import com.example.movies.viewmodel.MoviesActivityViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movies_detail.*

class MoviesDetailActivity : AppCompatActivity() {

    private val moviesActivityViewModel = MoviesActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_detail)

        val userId = intent.extras!!.getString("userId").toString()
        val movieId = intent.extras!!.getInt("movieId")
        moviesActivityViewModel.getMoviesDetail(movieId)

        changeBtn(userId, movieId.toString())

        add_favorites_btn.setOnClickListener {
            if (add_favorites_btn.text == "Удалить из избранного"){
                moviesActivityViewModel.removeFavorite(userId, movieId.toString())
                changeBtnBlue()
            }else{
                moviesActivityViewModel.writeFavorites(userId, movieId.toString())
                changeBtnRed()
            }

        }

        initObservers()

    }

    private fun changeBtnRed(){
        add_favorites_btn.setBackgroundColor(ContextCompat.getColor(this@MoviesDetailActivity, R.color.red))
        add_favorites_btn.setText("Удалить из избранного")
    }

    private fun changeBtnBlue(){
        add_favorites_btn.setBackgroundColor(ContextCompat.getColor(this@MoviesDetailActivity, R.color.purple_700))
        add_favorites_btn.setText("В избранное")
    }


    private fun changeBtn(userId: String, movieId: String) {
        moviesActivityViewModel.getFavoritesID(userId)
        moviesActivityViewModel.apply {
            moviesIdList.observe(this@MoviesDetailActivity) {
                if (it[0] != "null"){
                    if (movieId in it){
                        changeBtnRed()
                    }
                }
                add_favorites_btn.visibility = View.VISIBLE
            }
        }

    }


    private fun initObservers() {
        moviesActivityViewModel.apply {
            moviesDetail.observe(this@MoviesDetailActivity) {
                Log.i("Debug", "Присваиваем")
                Picasso.get().load(it.posterUrl).resize(1200, 1600).into(imageViewDetail)
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