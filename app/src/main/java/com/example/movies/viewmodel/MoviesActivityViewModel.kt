package com.example.movies.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.data.*
import com.example.movies.data.movies.*
import com.example.movies.repository.FirebaseRepository
import com.example.movies.repository.MoviesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesActivityViewModel {

    private val firebaseRepository = FirebaseRepository()

    private  val _movies = MutableLiveData<List<Item>>()
    val movies : LiveData<List<Item>> = _movies

    private  val _moviesFilter = MutableLiveData<List<Item>>()
    val moviesFilter : LiveData<List<Item>> = _moviesFilter

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

    private  val _images = MutableLiveData<List<ItemImage>>()
    val images : LiveData<List<ItemImage>> = _images

    private  val _awards = MutableLiveData<List<ItemAward>>()
    val awards : LiveData<List<ItemAward>> = _awards

    private  val _persons = MutableLiveData<List<PersonItem>>()
    val persons : LiveData<List<PersonItem>> = _persons

    private var moviesRecommendationSave = mutableListOf<Item>()
    private var moviesRecommendationSaveCommon = mutableListOf<Item>()
    var hashSet = HashSet<Item>()

    private var moviesFilterSave = mutableListOf<Item>()

    private var similarSave = mutableListOf<ItemSimilar>()

    private var moviesTopSave = listOf<Film>()

    private var moviesApi = MoviesApi.create()


    fun getMovies(page: Int, genres: Int){

        moviesApi.getMovies(page, genres).enqueue(object : Callback<MoviesData> {
            override fun onResponse(call: Call<MoviesData>, response: Response<MoviesData>) {
                moviesRecommendationSave += response.body()!!.items
                moviesRecommendationSave.shuffle()
                if (moviesRecommendationSave.size == 60) {
                    hashSet.addAll(moviesRecommendationSave)
                    moviesRecommendationSaveCommon += hashSet
                    moviesRecommendationSave.clear()
                    hashSet.clear()
                    _movies.postValue(moviesRecommendationSaveCommon)
                }
            }

            override fun onFailure(call: Call<MoviesData>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

    fun getMoviesFilter(page: Int, genres: Int, countries: Int, ratingFrom : Int, ratingTo: Int, yearFrom: Int, yearTo: Int, type: String){

        moviesApi.getMoviesFilter(page, genres, countries, ratingFrom, ratingTo, yearFrom, yearTo, type).enqueue(object : Callback<MoviesData> {
            override fun onResponse(call: Call<MoviesData>, response: Response<MoviesData>) {
                if (page == 1) moviesFilterSave.clear()
                if (response.body() != null) moviesFilterSave += response.body()!!.items
                _moviesFilter.postValue(moviesFilterSave)
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
        similarSave.clear()
    }

    fun getImages(movieId : Int){
        moviesApi.getImages(movieId).enqueue(object : Callback<Image> {
            override fun onResponse(call: Call<Image>, response: Response<Image>) {
                _images.postValue(response.body()!!.items)
            }

            override fun onFailure(call: Call<Image>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

    fun getAwards(movieId : Int){
        moviesApi.getAwards(movieId).enqueue(object : Callback<Award> {
            override fun onResponse(call: Call<Award>, response: Response<Award>) {
                _awards.postValue(response.body()!!.items)
            }

            override fun onFailure(call: Call<Award>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }

    fun getPerson(movieId : Int){
        moviesApi.getPerson(movieId).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                _persons.postValue(response.body())
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                Log.e("Debug", t.message.toString())
            }
        })
    }
}