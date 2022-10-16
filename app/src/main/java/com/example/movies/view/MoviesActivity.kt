package com.example.movies.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.view.adapter.CustomAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import kotlinx.android.synthetic.main.activity_movies.*


class MoviesActivity : AppCompatActivity(), CustomAdapter.ItemClickListener {

    private val moviesActivityViewModel = MoviesActivityViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        initObservers()

        moviesActivityViewModel.getMovies()
    }

    private fun initObservers(){
        Log.i("Debug", "observeMovies")
        moviesActivityViewModel.apply {
            movies.observe(this@MoviesActivity){
                recyclerView.adapter = CustomAdapter(it, this@MoviesActivity)
                Log.i("Debug", it.size.toString())

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }

    override fun onItemClick(id: Int) {
        val moviesDetailIntent = Intent(this@MoviesActivity, MoviesDetailActivity::class.java)
        moviesDetailIntent.putExtra("id", id)
        startActivity(moviesDetailIntent)
    }
}
