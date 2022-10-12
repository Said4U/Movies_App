package com.example.movies.data

import com.example.movies.data.movies.MoviesData
import com.example.movies.data.movies.OneMoviesData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import java.util.concurrent.TimeUnit


interface MoviesApi {
    @GET("/api/v2.2/films?order=RATING&type=ALL&ratingFrom=8&ratingTo=10&yearFrom=2012&yearTo=3000&page=4")
    @Headers("Content-Type: application/json",
        "X-API-KEY: 53746169-33b5-4540-854d-218df18cbb38")
    fun getMovies() : Call<MoviesData>

    @GET("/api/v2.2/films/{movieId}")
    @Headers("Content-Type: application/json",
        "X-API-KEY: 53746169-33b5-4540-854d-218df18cbb38")
    fun getOneMovie(@Path("movieId") movieId: Int?) : Call<OneMoviesData>

    companion object {

        private var BASE_URL = "https://kinopoiskapiunofficial.tech/"

        fun create() : MoviesApi {

            val okHttpClient: OkHttpClient = OkHttpClient.Builder().callTimeout(2, TimeUnit.SECONDS).build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(MoviesApi::class.java)

        }
    }}