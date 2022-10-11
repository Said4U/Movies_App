package com.example.films

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.films.data.MoviesApi
import com.example.films.data.movies.MoviesData
import com.example.films.data.movies.OneMoviesData
import kotlinx.android.synthetic.main.activity_movies.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MoviesActivity : AppCompatActivity(),  CustomAdapter.ItemClickListener{
    private var moviesApi = MoviesApi.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        Log.i("Debug", "OnCreate")

        getResponse()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }

    override fun onItemClick(id: Int) {
        Log.i("Debug", "Open")
        val moviesDetailIntent = Intent(this@MoviesActivity, MoviesDetailActivity::class.java)
        moviesDetailIntent.putExtra("id", id)
        startActivity(moviesDetailIntent)
    }

    fun getResponse(){
        moviesApi.getMovies().enqueue(object : Callback<MoviesData> {
            override fun onResponse(call: Call<MoviesData>, response: Response<MoviesData>) {

                Log.i("Debug", "onResponse")

                val adapter = CustomAdapter(response.body()?.items, this@MoviesActivity)
                recyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<MoviesData>, t: Throwable) {
                getResponse()
                Log.e("TAG", t.message.toString())
            }
        })
    }
}
