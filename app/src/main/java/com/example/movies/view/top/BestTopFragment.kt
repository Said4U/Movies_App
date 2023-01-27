package com.example.movies.view.top

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.view.MoviesDetailActivity
import com.example.movies.view.adapter.CustomAdapter
import com.example.movies.view.adapter.SearchMovieAdapter
import com.example.movies.view.adapter.TopAdapter
import com.example.movies.view.top.CardTopFragment
import com.example.movies.view.top.TopFragment
import com.example.movies.viewmodel.MoviesActivityViewModel
import kotlinx.android.synthetic.main.fragment_best_top.*
import kotlinx.android.synthetic.main.fragment_recommendation.*

class BestTopFragment : Fragment(R.layout.fragment_best_top), TopAdapter.ItemClickListener,
SearchMovieAdapter.ItemClickListener {


    private val moviesActivityViewModel = MoviesActivityViewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.topPlaceholder, CardTopFragment.newInstance())
                        commit()
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewTop.layoutManager = GridLayoutManager(requireContext(), 1)

        initObservers()

    }

    private fun initObservers(){
        moviesActivityViewModel.getTopMovies("TOP_250_BEST_FILMS")
        Log.i("Debug", "observeMovies")
        moviesActivityViewModel.apply {
            moviesTop.observe(viewLifecycleOwner){
                recyclerViewTop.adapter = TopAdapter(it, this@BestTopFragment)
                Log.i("topSize", it.size.toString())

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
        fun newInstance() = BestTopFragment()
    }
}