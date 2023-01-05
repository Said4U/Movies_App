package com.example.movies.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.view.adapter.CustomAdapter
import com.example.movies.view.adapter.MoviesDetailAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*


class FavoritesFragment : Fragment(R.layout.fragment_favorites), MoviesDetailAdapter.ItemClickListener {

    private val moviesActivityViewModel = MoviesActivityViewModel()


    companion object{
        fun getNewInstance(userId: Bundle?): FavoritesFragment {
            val favoritesFragment = FavoritesFragment()
            favoritesFragment.arguments = userId
            return favoritesFragment

        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
        initObservers()

        moviesActivityViewModel.getFavorites(arguments?.get("id") as String)
    }


    private fun initObservers(){
        Log.i("Debug", "observeMovies")
        moviesActivityViewModel.apply {
            moviesDetailList.observe(viewLifecycleOwner){
                Log.i("size", it.toString())
                recyclerViewFavorites.adapter = MoviesDetailAdapter(it, this@FavoritesFragment)
                Log.i("Debug", it.size.toString())

            }
        }
    }

    override fun onItemClick(id: Int) {
        val moviesDetailIntent = Intent(context, MoviesDetailActivity::class.java)
        moviesDetailIntent.putExtra("userId", id)
        moviesDetailIntent.putExtra("movieId", id)
        startActivity(moviesDetailIntent)
    }
}