package com.example.movies.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.view.adapter.PremieresAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import com.firebase.ui.auth.AuthUI.getApplicationContext
import kotlinx.android.synthetic.main.fragment_premiere.*
import kotlinx.android.synthetic.main.fragment_recommendation.*


class PremiereFragment : Fragment(R.layout.fragment_premiere), PremieresAdapter.ItemClickListener{

    private val moviesActivityViewModel = MoviesActivityViewModel()
    private val monthMap = mapOf(
        "Январь" to "JANUARY",
        "Февраль" to "FEBRUARY",
        "Март" to "MARCH",
        "Апрель" to "APRIL",
        "Май" to "MAY",
        "Июнь" to "JUNE",
        "Июль" to "JULY",
        "Август" to "AUGUST",
        "Сентябрь" to "SEPTEMBER",
        "Октябрь" to "OCTOBER",
        "Ноябрь" to "NOVEMBER",
        "Декабрь" to "DECEMBER",
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewPremiere.layoutManager = GridLayoutManager(requireContext(), 2)
        initObservers()
        spinnerMonth.setSelection(1)

        spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                Log.i("spinnerYear", parent!!.getItemAtPosition(selectedItemPosition).toString() + " " + monthMap[spinnerMonth.selectedItem.toString()]!!)
                moviesActivityViewModel.getPremieres(parent!!.getItemAtPosition(selectedItemPosition).toString(),
                    monthMap[spinnerMonth.selectedItem.toString()]!!)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                moviesActivityViewModel.getPremieres(spinnerYear.selectedItem.toString(),
                    monthMap[parent!!.getItemAtPosition(selectedItemPosition).toString()]!!)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initObservers(){
        Log.i("Debug", "observeMovies")
        moviesActivityViewModel.apply {
            moviesPremieres.observe(viewLifecycleOwner){
                recyclerViewPremiere.adapter = PremieresAdapter(it, this@PremiereFragment)
                Log.i("spinnerMonth", it?.size.toString())

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
        fun newInstance() = PremiereFragment()
    }
}