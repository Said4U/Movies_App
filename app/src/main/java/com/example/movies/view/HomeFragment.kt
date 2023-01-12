package com.example.movies.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.view.adapter.CustomAdapter
import com.example.movies.view.adapter.SearchMovieAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home), CustomAdapter.ItemClickListener,
    SearchMovieAdapter.ItemClickListener {


    private val moviesActivityViewModel = MoviesActivityViewModel()


    companion object{
        fun getNewInstance(userId: Bundle?): HomeFragment {
            val homeFragment = HomeFragment()
            homeFragment.arguments = userId
            return homeFragment
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    // Leave empty do disable back press or
                    // write your code which you want
//                    fragmentManager?.popBackStackImmediate()
                    initObservers()
                    Log.i("back22", "back")


                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        moviesActivityViewModel.getMovies(2, 2014)
        Log.i("Debug", "observeMovies")
        moviesActivityViewModel.apply {
            movies.observe(viewLifecycleOwner){
                recyclerView.adapter = CustomAdapter(it, this@HomeFragment)
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
                recyclerView.adapter = SearchMovieAdapter(it, this@HomeFragment)

            }
        }
    }

    override fun onItemClick(id: Int) {
        val moviesDetailIntent = Intent(context, MoviesDetailActivity::class.java)
        moviesDetailIntent.putExtra("userId", arguments?.get("id") as String)
        moviesDetailIntent.putExtra("movieId", id)
        startActivity(moviesDetailIntent)
    }

}