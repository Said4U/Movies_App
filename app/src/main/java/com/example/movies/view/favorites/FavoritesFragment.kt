package com.example.movies.view.favorites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.view.MoviesDetailActivity
import com.example.movies.view.adapter.FavoritesPreferencesAdapter
import com.example.movies.view.adapter.MoviesDetailAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_profile.*


class FavoritesFragment : Fragment(R.layout.fragment_favorites), MoviesDetailAdapter.ItemClickListener,
    FavoritesPreferencesAdapter.ItemClickListener {

    private val moviesActivityViewModel = MoviesActivityViewModel()
    private lateinit var userID : String
    private var currentFavoritesID = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewRecommendationFav.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val idPref = requireActivity().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
        userID = idPref.getString("userId", "").toString()


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
            if (moviesIdList.hasObservers()) moviesIdList.removeObservers(viewLifecycleOwner)

            moviesIdList.observe(viewLifecycleOwner) { movieIdList ->
                if (currentFavoritesID != movieIdList){
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

                    getSimilarList(movieIdList.shuffled().subList(0, maxLength))
                } else {
                    favoritesPrefTextView.visibility = View.GONE
                    fullPrefListText.visibility = View.GONE
                    fullFavoritesListText.visibility = View.GONE


                }
            }

            moviesDetailList.observe(viewLifecycleOwner) {
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