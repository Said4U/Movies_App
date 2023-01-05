package com.example.movies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.data.movies.Item
import com.example.movies.data.movies.MoviesData
import com.example.movies.data.movies.OneMoviesData
import com.example.movies.repository.FirebaseRepository
import com.example.movies.repository.MoviesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesActivityViewModel {

    private val firebaseRepository = FirebaseRepository()

    private  val _movies = MutableLiveData<List<Item>>()
    val movies : LiveData<List<Item>> = _movies

    private  val _moviesDetail = MutableLiveData<OneMoviesData>()
    val moviesDetail : LiveData<OneMoviesData> = _moviesDetail

    private  val _moviesDetailList = MutableLiveData<List<OneMoviesData>>()
    val moviesDetailList : LiveData<List<OneMoviesData>> = _moviesDetailList

    private var moviesApi = MoviesApi.create()


    fun getMovies(page: Int, yearTo: Int){

        moviesApi.getMovies(page).enqueue(object : Callback<MoviesData> {
            override fun onResponse(call: Call<MoviesData>, response: Response<MoviesData>) {
                Log.i("Debug", "getMovies")
                _movies.postValue(response.body()?.items)

            }

            override fun onFailure(call: Call<MoviesData>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

    fun getMoviesDetail(id: Int){

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

    fun writeFavorites(userId: String, movieId: String) {

        firebaseRepository.writeFavorites(userId, movieId)
    }

    fun getFavorites(userId: String) {
        firebaseRepository.getFavorites(userId).addOnCompleteListener {
            val moviesIdLst = (it.result.value  as Map<String, String>).values.toMutableList()
            Log.i("getFavorites", "getFavorites")

            val moviesItemsLst = ArrayList<OneMoviesData>()

            for (movieId in moviesIdLst){
                moviesApi.getOneMovie(movieId.toInt()).enqueue(object : Callback<OneMoviesData> {
                    override fun onResponse(call: Call<OneMoviesData>, response: Response<OneMoviesData>) {
                        Log.i("moviesIdLst", "3 $moviesItemsLst")
                        moviesItemsLst.add(response.body()!!)
                        _moviesDetailList.postValue(moviesItemsLst)
                    }
                    override fun onFailure(call: Call<OneMoviesData>, t: Throwable) {
                        Log.e("Debug", t.message.toString())
                    }

                })

            }
        }




    }

}