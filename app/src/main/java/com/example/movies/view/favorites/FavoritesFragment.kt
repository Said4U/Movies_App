package com.example.movies.view.favorites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.view.MoviesDetailActivity
import com.example.movies.view.adapter.FavoritesPreferencesAdapter
import com.example.movies.view.adapter.MoviesDetailAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*


class FavoritesFragment : Fragment(R.layout.fragment_favorites), MoviesDetailAdapter.ItemClickListener,
    FavoritesPreferencesAdapter.ItemClickListener {

    private val moviesActivityViewModel = MoviesActivityViewModel()
    private lateinit var userID : String
    private lateinit var userName : String
    private var currentFavoritesID = mutableListOf<String>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewRecommendationFav.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val idPref = requireActivity().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
        userID = idPref.getString("userId", "").toString()
        userName = idPref.getString("name", "").toString()

        initObservers()

        fullFavoritesListText.setOnClickListener {
            val intent = Intent(context, FavoritesListActivity::class.java)
            startActivity(intent)
        }

        fullPrefListText.setOnClickListener {
            val intent = Intent(context, FavoritesPreferencesListActivity::class.java)
            startActivity(intent)
        }

    }

    private fun initObservers(){
        moviesActivityViewModel.getFavoritesID(userID)

        moviesActivityViewModel.apply {
            moviesIdList.observe(viewLifecycleOwner) { movieIdList ->
                if (currentFavoritesID != movieIdList){
                    Log.i("getSimilar", movieIdList.toString())
                    currentFavoritesID = movieIdList
                    moviesActivityViewModel.clearFavorites()
                    moviesActivityViewModel.getFavorites(userID)
                }
                val maxLength = if (movieIdList.size >= 3) 3
                else movieIdList.size

                // похожие фильмы
                if (movieIdList[0] != "null"){
                    favoritesPrefTextView.visibility = View.VISIBLE
                    fullPrefListText.visibility = View.VISIBLE
                    fullFavoritesListText.visibility = View.VISIBLE

                    moviesActivityViewModel.getSimilarList(movieIdList.shuffled().subList(0, maxLength))
                } else {
                    favoritesPrefTextView.visibility = View.GONE
                    fullPrefListText.visibility = View.GONE
                    fullFavoritesListText.visibility = View.GONE


                }
            }

            moviesDetailList.observe(viewLifecycleOwner) {
                Log.i("moviesDetailList", it.toString())
                recyclerViewFavorites.adapter = MoviesDetailAdapter(it, this@FavoritesFragment)

            }

            moviesSimilar.observe(viewLifecycleOwner) {
                val maxLengthPref = if (it.size >= 10) 10
                else it.size
                recyclerViewRecommendationFav.adapter = FavoritesPreferencesAdapter(it.subList(0, maxLengthPref), this@FavoritesFragment)
            }
        }
    }

    override fun onItemClick(id: Int) {
        val moviesDetailIntent = Intent(context, MoviesDetailActivity::class.java)
        moviesDetailIntent.putExtra("userId", userID)
        moviesDetailIntent.putExtra("movieId", id)
        startActivity(moviesDetailIntent)
    }
}