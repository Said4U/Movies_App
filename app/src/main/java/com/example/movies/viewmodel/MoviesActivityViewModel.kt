package com.example.movies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.data.movies.Item
import com.example.movies.data.movies.MoviesData
import com.example.movies.data.movies.OneMoviesData
import com.example.movies.repository.MoviesApi
import kotlinx.android.synthetic.main.activity_movies_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesActivityViewModel {

    private  val _movies = MutableLiveData<List<Item>>()
    val movies : LiveData<List<Item>> = _movies

    private  val _moviesDetail = MutableLiveData<OneMoviesData>()
    val moviesDetail : LiveData<OneMoviesData> = _moviesDetail

    private var moviesApi = MoviesApi.create()


    fun getMovies(){

        moviesApi.getMovies().enqueue(object : Callback<MoviesData> {
            override fun onResponse(call: Call<MoviesData>, response: Response<MoviesData>) {
                Log.i("Debug", "getMovies")
                _movies.postValue(response.body()?.items)

            }

            override fun onFailure(call: Call<MoviesData>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

    fun getMoviesDetail(id: Int?){

        moviesApi.getOneMovie(id).enqueue(object : Callback<OneMoviesData> {
            override fun onResponse(call: Call<OneMoviesData>, response: Response<OneMoviesData>) {
                Log.i("Debug", "getMoviesDetail")

                _moviesDetail.postValue(response.body())
            }

            override fun onFailure(call: Call<OneMoviesData>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }


}