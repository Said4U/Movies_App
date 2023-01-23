package com.example.movies.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.view.adapter.CustomAdapter
import com.example.movies.view.adapter.SearchMovieAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_recommendation.*

class RecommendationFragment : Fragment(R.layout.fragment_recommendation), CustomAdapter.ItemClickListener,
    SearchMovieAdapter.ItemClickListener {


    private val moviesActivityViewModel = MoviesActivityViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("replace", "Reccom")

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        initObservers()


        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchMovies(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initObservers(){
        moviesActivityViewModel.getMovies(2)
        Log.i("Debug", "observeMovies")
        moviesActivityViewModel.apply {
            movies.observe(viewLifecycleOwner){
                recyclerView.adapter = CustomAdapter(it, this@RecommendationFragment)
                Log.i("Debug", it.size.toString())

            }
        }
    }

    private fun searchMovies(keyword: String) {

        moviesActivityViewModel.getSearchMovie(keyword)
        moviesActivityViewModel.apply {
            moviesSearch.observe(viewLifecycleOwner) {
                if (it.isEmpty()){
                    Toast.makeText(context, "Ничего не нашлось", Toast.LENGTH_SHORT).show()
                }
                recyclerView.adapter = SearchMovieAdapter(it, this@RecommendationFragment)

            }
        }
    }

    override fun onItemClick(id: Int) {
        val moviesDetailIntent = Intent(context, MoviesDetailActivity::class.java)
        moviesDetailIntent.putExtra("userId", arguments?.getString("id"))
        moviesDetailIntent.putExtra("movieId", id)
        startActivity(moviesDetailIntent)
    }

    companion object {
        fun newInstance() = RecommendationFragment()
    }
}