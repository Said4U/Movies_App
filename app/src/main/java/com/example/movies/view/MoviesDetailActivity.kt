package com.example.movies.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.view.adapter.AwardsAdapter
import com.example.movies.view.adapter.PersonAdapter
import com.example.movies.view.adapter.VideoAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movies_detail.*
import kotlinx.android.synthetic.main.card_view_design.view.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.top_card_design.view.*


class MoviesDetailActivity : AppCompatActivity(), VideoAdapter.ItemClickListener {

    private val moviesActivityViewModel = MoviesActivityViewModel()
    var movieId = 0
    private val filmTypeMap = mapOf(
        "FILM" to "Фильм",
        "VIDEO" to "Видеоролик",
        "TV_SERIES" to "Сериал",
        "MINI_SERIES" to "Мини-сериал",
        "TV_SHOW " to "Шоу"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_detail)

        val userId = intent.extras!!.getString("userId").toString()
        movieId = intent.extras!!.getInt("movieId")

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
        add_favorites_btn.setBackgroundColor(ContextCompat.getColor(this@MoviesDetailActivity, R.color.orange))
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


    @SuppressLint("SetTextI18n")
    private fun initObservers() {
        moviesActivityViewModel.apply {
            moviesDetail.observe(this@MoviesDetailActivity) {
                Picasso.get().load(it.posterUrl).fit().into(imageViewDetail)
                singleMovieNameRus.text = it?.nameRu?: it.nameEn?: it.nameOriginal
                singleMovieNameEn.text = it.nameEn?: it.nameOriginal
                slogan.text = it.slogan?: ""
                val ratingDigit = it.ratingKinopoisk
                when(ratingDigit){
                    in 7.0..10.0 -> rating.setBackgroundColor(Color.parseColor("#32CD32"))
                    in 5.0..6.9 -> rating.setBackgroundColor(Color.GRAY)
                    else -> rating.movies_mark.setBackgroundColor(Color.RED)
                }
                rating.text = ratingDigit.toString()

                if (slogan.text == ""){
                    slogan.visibility = View.GONE
                }

                if (singleMovieNameEn.text == ""){
                    singleMovieNameEn.visibility = View.GONE
                }

                var genresString = ""
                 it.genres.forEach{ genreObject ->
                     genresString += genreObject.genre + ", "
                 }
                genresText.text = genresString.removeRange(genresString.length - 2, genresString.length - 1).replaceFirstChar { char -> char.uppercase() }

                var countryYearString = ""
                it.countries.forEach{ countryObject ->
                    countryYearString += countryObject.country + ", "
                }
                countryYearText.text = countryYearString.removeRange(countryYearString.length - 2, countryYearString.length - 1) + "• " + it.year

                if (it.description.length >= 150){
                    descriptionText.text = it.description.subSequence(0, 150).toString() + " ..."
                } else {
                    descriptionText.text = it.description
                }

                var infoString = filmTypeMap[it.type]
                infoString += ", " + it.filmLength + " мин."
                otherInfo.text = infoString

                fullDescriptionText.setOnClickListener{_ ->
                    descriptionText.text = it.description
                }

                watchText.setOnClickListener { _ ->
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.webUrl))
                    startActivity(browserIntent)
                }
            }

            getImages(movieId)
            images.observe(this@MoviesDetailActivity) {
                val maxLength = if (it.size >= 10) 10
                else it.size

                var position = -1
                it.subList(0, maxLength).forEach{itemImage ->
                    position++
                    val image = linearLayout.getChildAt(position) as ImageView
                    Picasso.get().load(itemImage.imageUrl).fit().into(image)
                }
            }

            getVideos(movieId)
            video.observe(this@MoviesDetailActivity) {
                recyclerViewVideo.layoutManager = LinearLayoutManager(this@MoviesDetailActivity, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewVideo.adapter = VideoAdapter(it, this@MoviesDetailActivity)
            }

            getAwards(movieId)
            awards.observe(this@MoviesDetailActivity) {
                recyclerViewAwards.layoutManager = LinearLayoutManager(this@MoviesDetailActivity, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewAwards.adapter = AwardsAdapter(it)
                if (it.isEmpty()) {
                    var awardString = awardsText.text.toString()
                    awardString += " (отсутствуют)"
                    awardsText.text = awardString
                }
            }

            getPerson(movieId)
            persons.observe(this@MoviesDetailActivity) {
                recyclerViewPerson.layoutManager = LinearLayoutManager(this@MoviesDetailActivity, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewPerson.adapter = PersonAdapter(it)
            }
        }
    }

    override fun onItemClick(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }


}