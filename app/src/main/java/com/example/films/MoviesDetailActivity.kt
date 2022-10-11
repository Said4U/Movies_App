package com.example.films

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.films.data.MoviesApi
import com.example.films.data.movies.OneMoviesData
import kotlinx.android.synthetic.main.activity_movies_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_detail)

        val moviesApi = MoviesApi.create()
        val id = intent.extras?.getInt("id")

        moviesApi.getOneMovie(id).enqueue(object : Callback<OneMoviesData> {
            override fun onResponse(call: Call<OneMoviesData>, response: Response<OneMoviesData>) {
                singleMovieName.text = response.body()?.nameRu ?: response.body()?.nameEn
            }

            override fun onFailure(call: Call<OneMoviesData>, t: Throwable) {
                Log.e("TAG", t.message.toString())
            }
        })
    }
}