package com.example.movies.repository

import com.example.movies.Constants
import com.example.movies.data.Video
import com.example.movies.data.movies.SearchMovie
import com.example.movies.data.movies.MoviesData
import com.example.movies.data.movies.OneMoviesData
import com.example.movies.data.movies.TopMovies
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface MoviesApi {
    @GET("/api/v2.2/films?order=RATING&type=ALL&ratingFrom=0&ratingTo=10&yearFrom=2016&yearTo=3000")
    @Headers(
        "accept: application/json",
        "X-API-KEY: " + Constants.API_KEY )
    fun getMovies(@Query("page") page : Int) : Call<MoviesData>

    @GET("/api/v2.2/films/{movieId}")
    @Headers("Content-Type: application/json",
        "X-API-KEY: " + Constants.API_KEY )
    fun getOneMovie(@Path("movieId") movieId: Int) : Call<OneMoviesData>

    @GET("/api/v2.1/films/search-by-keyword?page=1")
    @Headers("Content-Type: application/json",
        "X-API-KEY: " + Constants.API_KEY )
    fun getSearchMovie(@Query("keyword") keyword : String) : Call<SearchMovie>

    @GET("/api/v2.2/films/{movieId}/videos")
    @Headers("Content-Type: application/json",
        "X-API-KEY: " + Constants.API_KEY )
    fun getVideos(@Path("movieId") movieId: Int) : Call<Video>

    @GET("api/v2.2/films/top?page=1")
    @Headers("Content-Type: application/json",
        "X-API-KEY: " + Constants.API_KEY )
    fun getTopMovies(@Query("type") type: String) : Call<TopMovies>

    companion object {

        private var BASE_URL = "https://kinopoiskapiunofficial.tech/"

        fun create() : MoviesApi {

            val okHttpClient: OkHttpClient = OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(MoviesApi::class.java)

        }
    }}