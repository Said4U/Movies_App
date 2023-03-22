package com.example.movies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.data.ItemSimilar
import com.example.movies.data.Similar
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

    private  val _genresPreferences = MutableLiveData<ArrayList<String>>()
    val genresPreferences : LiveData<ArrayList<String>> = _genresPreferences

    private  val _moviesSimilar = MutableLiveData<List<ItemSimilar>>()
    val moviesSimilar : LiveData<List<ItemSimilar>> = _moviesSimilar

    private var moviesRecommendationSave = mutableListOf<Item>()
    private var moviesRecommendationSaveCommon = mutableListOf<Item>()

    private var similarSave = mutableListOf<ItemSimilar>()

    private var moviesTopSave = listOf<Film>()

    private var moviesApi = MoviesApi.create()


    fun getMovies(page: Int, genres: Int){

        moviesApi.getMovies(page, genres).enqueue(object : Callback<MoviesData> {
            override fun onResponse(call: Call<MoviesData>, response: Response<MoviesData>) {
                Log.i("Debug", "getMovies")
                moviesRecommendationSave += response.body()!!.items
                moviesRecommendationSave.shuffle()
                if (moviesRecommendationSave.size == 60){
                    moviesRecommendationSaveCommon += moviesRecommendationSave
                    moviesRecommendationSave.clear()
                    _movies.postValue(moviesRecommendationSaveCommon)
                }
            }

            override fun onFailure(call: Call<MoviesData>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

    fun getMoviesDetail(id: Int){

        moviesApi.getOneMovie(id).enqueue(object : Callback<OneMoviesData> {
            override fun onResponse(call: Call<OneMoviesData>, response: Response<OneMoviesData>) {
                _moviesDetail.postValue(response.body())
            }

            override fun onFailure(call: Call<OneMoviesData>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

    fun getMoviesDetailRecommendation(id: Int){

        moviesApi.getOneMovie(id).enqueue(object : Callback<OneMoviesData> {
            override fun onResponse(call: Call<OneMoviesData>, response: Response<OneMoviesData>) {
                Log.i("response", response.body().toString())
                Log.i("response", id.toString())
                val body = response.body()!!
                moviesRecommendationSaveCommon.add(Item(
                    body.countries,
                    body.genres,
                    body.imdbId,
                    body.kinopoiskId,
                    body.nameEn,
                    body.nameOriginal,
                    body.nameRu,
                    body.posterUrl,
                    body.posterUrlPreview,
                    body.ratingImdb,
                    body.ratingKinopoisk,
                    body.type,
                    body.year,
                    ""
                ))

                _movies.postValue(moviesRecommendationSaveCommon)
            }

            override fun onFailure(call: Call<OneMoviesData>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

    fun writeFavorites(userId: String, movieId: String) {
        firebaseRepository.writeFavorites(userId, movieId)
    }

    fun writeGenresPreferences(userId: String, genresList: List<String>) {
        firebaseRepository.writeGenresPreferences(userId, genresList)
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

    fun getGenresPreferences(userId: String){
        firebaseRepository.getGenresPreferences(userId).addOnCompleteListener {
            if (it.result.value != null) {
                _genresPreferences.postValue((it.result.value as ArrayList<String>?))
            }else{
                _genresPreferences.postValue(arrayListOf("null"))
            }
        }
    }

    fun getFavorites(userId: String) {
        firebaseRepository.getFavorites(userId).addOnCompleteListener {

            if (it.result.value != null){
                val moviesIdLst = (it.result.value  as Map<String, String>).values.toMutableList()
                val maxLength = if (moviesIdLst.size >= 6) 6
                else moviesIdLst.size

                getFavoritesItem(moviesIdLst, maxLength)
            } else {
                _moviesDetailList.postValue(mutableListOf())

            }
        }
    }

    fun getFavoritesFull(userId: String) {
        firebaseRepository.getFavorites(userId).addOnCompleteListener {

            if (it.result.value != null) {
                val moviesIdLst = (it.result.value as Map<String, String>).values.toMutableList()
                getFavoritesItem(moviesIdLst, moviesIdLst.size)
            }
        }
    }

    private fun getFavoritesItem(moviesIdLst: MutableList<String>, maxLength: Int) {

        val moviesItemsLst = ArrayList<OneMoviesData>()

        for (movieId in moviesIdLst.subList(0,  maxLength)){
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

    fun getSimilar(movieId: Int){
        moviesApi.getSimilar(movieId).enqueue(object : Callback<Similar> {
            override fun onResponse(call: Call<Similar>, response: Response<Similar>) {

                _moviesSimilar.postValue(response.body()!!.items)
            }

            override fun onFailure(call: Call<Similar>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

    fun getSimilarList(movieIdList: List<String>){
        for (movieId in movieIdList) {
            moviesApi.getSimilar(movieId.toInt()).enqueue(object : Callback<Similar> {
                override fun onResponse(call: Call<Similar>, response: Response<Similar>) {
                    similarSave.addAll(response.body()!!.items)
                    _moviesSimilar.postValue(similarSave.shuffled())
                }

                override fun onFailure(call: Call<Similar>, t: Throwable) {
                    Log.e("Debug", t.message.toString())
                }
            })
        }
    }

}