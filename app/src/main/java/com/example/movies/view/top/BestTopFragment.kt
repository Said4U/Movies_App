package com.example.movies.view.top

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.view.MoviesDetailActivity
import com.example.movies.view.ProfileFragment
import com.example.movies.view.adapter.SearchMovieAdapter
import com.example.movies.view.adapter.TopAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import kotlinx.android.synthetic.main.fragment_best_top.*
import kotlinx.android.synthetic.main.fragment_recommendation.*


class BestTopFragment : Fragment(R.layout.fragment_best_top), TopAdapter.ItemClickListener,
SearchMovieAdapter.ItemClickListener {


    private val moviesActivityViewModel = MoviesActivityViewModel()

    var count = 1

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

        recyclerViewTop.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (count < 13){
                        count++
                        moviesActivityViewModel.getTopMovies(requireArguments().getString("top")!!, count)
                    }
                }
            }
        })
    }

    private fun initObservers(){
        moviesActivityViewModel.getTopMovies(requireArguments().getString("top")!!, 1)
        Log.i("Debug", "observeMovies")
        moviesActivityViewModel.apply {
            moviesTop.observe(viewLifecycleOwner){
                val recyclerViewState = recyclerViewTop.layoutManager!!.onSaveInstanceState()
                recyclerViewTop.adapter = TopAdapter(it, this@BestTopFragment)
                recyclerViewTop.layoutManager!!.onRestoreInstanceState(recyclerViewState)
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
        fun newInstance(topName: Bundle?): BestTopFragment {
            val bestTopFragment = BestTopFragment()
            bestTopFragment.arguments = topName
            return bestTopFragment
        }
    }
}