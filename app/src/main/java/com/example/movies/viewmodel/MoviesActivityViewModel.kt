package com.example.movies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.data.Video
import com.example.movies.data.VideoItem
import com.example.movies.data.movies.*
import com.example.movies.repository.FirebaseRepository
import com.example.movies.repository.MoviesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Year

class MoviesActivityViewModel {

    private val firebaseRepository = FirebaseRepository()

    private  val _movies = MutableLiveData<List<Item>>()
    val movies : LiveData<List<Item>> = _movies

    private  val _moviesDetail = MutableLiveData<OneMoviesData>()
    val moviesDetail : LiveData<OneMoviesData> = _moviesDetail

    private  val _moviesDetailList = MutableLiveData<List<OneMoviesData>>()
    val moviesDetailList : LiveData<List<OneMoviesData>> = _moviesDetailList

    private  val _moviesSearch = MutableLiveData<List<Film>>()
    val moviesSearch : LiveData<List<Film>> = _moviesSearch

    private  val _moviesIdList = MutableLiveData<MutableList<String>>()
    val moviesIdList : LiveData<MutableList<String>> = _moviesIdList

    private  val _video = MutableLiveData<List<VideoItem>>()
    val video : LiveData<List<VideoItem>> = _video

    private  val _moviesTop = MutableLiveData<List<Film>>()
    val moviesTop : LiveData<List<Film>> = _moviesTop

    private  val _moviesPremieres = MutableLiveData<List<Item>>()
    val moviesPremieres : LiveData<List<Item>> = _moviesPremieres

    private var moviesRecommendationSave = listOf<Item>()

    private var moviesTopSave = listOf<Film>()

    private var moviesApi = MoviesApi.create()


    fun getMovies(page: Int){

        moviesApi.getMovies(page).enqueue(object : Callback<MoviesData> {
            override fun onResponse(call: Call<MoviesData>, response: Response<MoviesData>) {
                Log.i("Debug", "getMovies")
                moviesRecommendationSave = moviesRecommendationSave + response.body()!!.items
                _movies.postValue(moviesRecommendationSave)
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

    fun removeFavorite(userId: String, movieId: String) {
        firebaseRepository.removeFavorite(userId, movieId)
    }

     fun getFavoritesID(userId: String){
         firebaseRepository.getFavorites(userId).addOnCompleteListener {
             if (it.result.value != null) {
                 _moviesIdList.postValue((it.result.value as Map<String, String>).values.toMutableList())
             }else{
                 _moviesIdList.postValue(mutableListOf("null"))
             }
         }
     }

    fun clearFavorites(){
        _moviesDetailList.postValue(mutableListOf())
    }

    fun getFavorites(userId: String) {
        firebaseRepository.getFavorites(userId).addOnCompleteListener {

            if (it.result.value != null){
                val moviesIdLst = (it.result.value  as Map<String, String>).values.toMutableList()

                val moviesItemsLst = ArrayList<OneMoviesData>()

                for (movieId in moviesIdLst){
                    moviesApi.getOneMovie(movieId.toInt()).enqueue(object : Callback<OneMoviesData> {
                        override fun onResponse(call: Call<OneMoviesData>, response: Response<OneMoviesData>) {
                            moviesItemsLst.add(response.body()!!)
                            _moviesDetailList.postValue(moviesItemsLst)
                        }
                        override fun onFailure(call: Call<OneMoviesData>, t: Throwable) {
                            Log.e("Debug", t.message.toString())
                        }
                    })
                }
            }else{
                _moviesDetailList.postValue(mutableListOf())

            }
        }
    }

    fun getSearchMovie(keyword : String){

        moviesApi.getSearchMovie(keyword).enqueue(object : Callback<SearchMovie> {
            override fun onResponse(call: Call<SearchMovie>, response: Response<SearchMovie>) {
                _moviesSearch.postValue(response.body()!!.films)
            }

            override fun onFailure(call: Call<SearchMovie>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

    fun getVideos(movieId : Int){

        moviesApi.getVideos(movieId).enqueue(object : Callback<Video> {
            override fun onResponse(call: Call<Video>, response: Response<Video>) {
                _video.postValue(response.body()!!.items)
            }

            override fun onFailure(call: Call<Video>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

    fun getTopMovies(topType : String, page: Int){

        moviesApi.getTopMovies(topType, page).enqueue(object : Callback<TopMovies> {
            override fun onResponse(call: Call<TopMovies>, response: Response<TopMovies>) {
                moviesTopSave = moviesTopSave + response.body()!!.films
                _moviesTop.postValue(moviesTopSave)
            }

            override fun onFailure(call: Call<TopMovies>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

    fun getPremieres(year : String, month: String){

        moviesApi.getPremieres(year, month).enqueue(object : Callback<Premieres> {
            override fun onResponse(call: Call<Premieres>, response: Response<Premieres>) {
                _moviesPremieres.postValue(response.body()?.items)
            }

            override fun onFailure(call: Call<Premieres>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

}