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
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.view.adapter.CustomAdapter
import com.example.movies.view.adapter.SearchMovieAdapter
import com.example.movies.view.adapter.TopAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_best_top.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_recommendation.*

class RecommendationFragment : Fragment(R.layout.fragment_recommendation), CustomAdapter.ItemClickListener,
    SearchMovieAdapter.ItemClickListener {


    private val moviesActivityViewModel = MoviesActivityViewModel()
    var count = 1


    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    initObservers()

                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        searchView.background = ResourcesCompat.getDrawable(resources, R.drawable.corners_white, null)

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
        moviesActivityViewModel.getMovies(1)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (count < 5){
                        count++
                        moviesActivityViewModel.getMovies(count)
                    }
                }
            }
        })

        Log.i("Debug", "observeMovies")
        moviesActivityViewModel.apply {
            movies.observe(viewLifecycleOwner){
                val recyclerViewState = recyclerView.layoutManager!!.onSaveInstanceState()
                recyclerView.adapter = CustomAdapter(it, this@RecommendationFragment)
                recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)

                Log.i("Debug", it.size.toString())

            }
        }
    }

    private fun searchMovies(keyword: String) {

        moviesActivityViewModel.getSearchMovie(keyword)

        recyclerView.clearOnScrollListeners()

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
        val idPref = requireActivity().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)

        val userID = idPref.getString("userId", "")

        val moviesDetailIntent = Intent(context, MoviesDetailActivity::class.java)
        moviesDetailIntent.putExtra("userId", userID)
        moviesDetailIntent.putExtra("movieId", id)
        startActivity(moviesDetailIntent)
    }

    companion object {
        fun newInstance() = RecommendationFragment()
    }
}