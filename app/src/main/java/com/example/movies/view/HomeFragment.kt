package com.example.movies.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.view.adapter.CustomAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home), CustomAdapter.ItemClickListener {


    private val moviesActivityViewModel = MoviesActivityViewModel()


    companion object{
        fun getNewInstance(userId: Bundle?): HomeFragment {
            val homeFragment = HomeFragment()
            homeFragment.arguments = userId
            return homeFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        initObservers()

        moviesActivityViewModel.getMovies(2, 2014)

    }

    private fun initObservers(){
        Log.i("Debug", "observeMovies")
        moviesActivityViewModel.apply {
            movies.observe(viewLifecycleOwner){
                recyclerView.adapter = CustomAdapter(it, this@HomeFragment)
                Log.i("Debug", it.size.toString())

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