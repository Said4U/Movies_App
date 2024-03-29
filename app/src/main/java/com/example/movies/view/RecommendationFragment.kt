package com.example.movies.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import java.util.Objects

class RecommendationFragment : Fragment(R.layout.fragment_recommendation), CustomAdapter.ItemClickListener,
    SearchMovieAdapter.ItemClickListener {


    private val moviesActivityViewModel = MoviesActivityViewModel()
    var count = 1
    var countFilter = 1
    var isFilter = false
    private lateinit var idPref: SharedPreferences
    val processedData = ArrayList<String>()
    private lateinit var userID : String

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.extras?.getStringArrayList("filterList")

            processedData.clear()
            processedData.add(genreMap[data!![0].replaceFirstChar { genre -> genre.lowercase() }]!!.toString())
            processedData.add(countryMap[data[1]]!!.toString())
            processedData.add(data[2])
            processedData.add(data[3])
            processedData.add(data[4])
            processedData.add(data[5])
            processedData.add(typeMap[data[6]]!!)
            moviesActivityViewModel.getMoviesFilter(
                1,
                processedData[0].toInt(),
                processedData[1].toInt(),
                processedData[2].toInt(),
                processedData[3].toInt(),
                processedData[4].toInt(),
                processedData[5].toInt(),
                processedData[6]
            )
            isFilter = true

        }
        if (result.resultCode == Activity.RESULT_FIRST_USER) {
            recyclerView.adapter = CustomAdapter(
                moviesActivityViewModel.movies.value,
                this@RecommendationFragment
            )
            isFilter = false
        }
    }

    private val genreMap = mapOf(
        "триллер" to 1,
        "драма" to 2,
        "криминал" to 3,
        "мелодрама" to 4,
        "детектив" to 5,
        "фантастика" to 6,
        "приключения" to 7,
        "биография" to 8,
        "фильм-нуар" to 9,
        "вестерн" to 10,
        "боевик" to 11,
        "фэнтези" to 12,
        "комедия" to 13,
        "военный" to 14,
        "история" to 15,
        "музыка" to 16,
        "ужасы" to 17,
        "мультфильм" to 18,
        "семейный" to 19,
        "мюзикл" to 20,
        "спорт" to 21,
        "документальный" to 22,
        "короткометражка" to 23,
        "аниме" to 24,
        "новости" to 26,
        "концерт" to 27,
        "для взрослых" to 28,
        "церемония" to 29,
        "реальное ТВ" to 30,
        "игра" to 31,
        "ток-шоу" to 32,
        "детский" to 33,
    )

    private val countryMap = mapOf(
        "США" to 1,
        "Швейцария" to 2,
        "Франция" to 3,
        "Польша" to 4,
        "Великобритания" to 5,
        "Швеция" to 6,
        "Индия" to 7,
        "Испания" to 8,
        "Германия" to 9,
        "Италия" to 10,
        "Гонконг" to 11,
        "Германия (ФРГ)" to 12,
        "Австралия" to 13,
        "Канада" to 14,
        "Мексика" to 15,
        "Япония" to 16,
        "Дания" to 17,
        "Чехия" to 18,
        "Ирландия" to 19,
        "Люксембург" to 20,
        "Китай" to 21,
        "Норвегия" to 22,
        "Нидерланды" to 23,
        "Аргентина" to 24,
        "Финляндия" to 25,
        "Босния и Герцеговина" to 26,
        "Австрия" to 27,
        "Тайвань" to 28,
        "Новая Зеландия" to 29,
        "Бразилия" to 30,
        "Чехословакия" to 31,
        "Мальта" to 32,
        "СССР" to 33,
        "Россия" to 34,
    )

    private val typeMap = mapOf(
        "Все" to "ALL",
        "Фильм" to "FILM",
        "Шоу" to "TV_SHOW",
        "Сериал" to "TV_SERIES",
        "Мини-сериал" to "MINI_SERIES",
    )


    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    recyclerView.adapter = CustomAdapter(
                        moviesActivityViewModel.movies.value,
                        this@RecommendationFragment)
                    isFilter = false
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        idPref = requireActivity().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
        userID = idPref.getString("userId", "").toString()

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

        filterBtn.setOnClickListener {
            startForResult.launch(Intent(context, FilterActivity::class.java))
        }
    }

    private fun initObservers(){
        // запрос жанров к БД
        moviesActivityViewModel.getGenresPreferences(userID)

        // запрос предпочитаемых фильмов по жанрам
        moviesActivityViewModel.apply {
            genresPreferences.observe(viewLifecycleOwner) {
                moviesActivityViewModel.getMovies(
                    1,
                    genreMap[it[0].replaceFirstChar { genre -> genre.lowercase() }]!!,
                )
                moviesActivityViewModel.getMovies(
                    1,
                    genreMap[it[1].replaceFirstChar { genre -> genre.lowercase() }]!!,
                )
                moviesActivityViewModel.getMovies(
                    1,
                    genreMap[it[2].replaceFirstChar { genre -> genre.lowercase() }]!!,
                )
            }

            genresPreferences.observe(viewLifecycleOwner) {
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (!recyclerView.canScrollVertically(1)) {
                            if (isFilter) {
                                if (countFilter < 5) {
                                    countFilter++
                                    moviesActivityViewModel.getMoviesFilter(
                                        countFilter,
                                        processedData[0].toInt(),
                                        processedData[1].toInt(),
                                        processedData[2].toInt(),
                                        processedData[3].toInt(),
                                        processedData[4].toInt(),
                                        processedData[5].toInt(),
                                        processedData[6]
                                    )
                                }
                            } else {
                                if (count < 5) {
                                    count++
                                    Log.i("canScrollVertically", "canScrollVertically")
                                    moviesActivityViewModel.getMovies(
                                        count,
                                        genreMap[it[0].replaceFirstChar { genre -> genre.lowercase() }]!!
                                    )
                                    moviesActivityViewModel.getMovies(
                                        count,
                                        genreMap[it[1].replaceFirstChar { genre -> genre.lowercase() }]!!
                                    )
                                    moviesActivityViewModel.getMovies(
                                        count,
                                        genreMap[it[2].replaceFirstChar { genre -> genre.lowercase() }]!!
                                    )
                                }
                            }
                        }
                    }
                })
            }

            movies.observe(viewLifecycleOwner){
                val recyclerViewState = recyclerView.layoutManager!!.onSaveInstanceState()
                recyclerView.adapter = CustomAdapter(it, this@RecommendationFragment)
                recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
            }

            moviesFilter.observe(viewLifecycleOwner){
                val recyclerViewState = recyclerView.layoutManager!!.onSaveInstanceState()
                if (it.isEmpty()) Toast.makeText(context, "Ничего не нашлось", Toast.LENGTH_SHORT).show()
                recyclerView.adapter = CustomAdapter(it, this@RecommendationFragment)
                recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)            }
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
        val moviesDetailIntent = Intent(context, MoviesDetailActivity::class.java)
        moviesDetailIntent.putExtra("userId", userID)
        moviesDetailIntent.putExtra("movieId", id)
        startActivity(moviesDetailIntent)
    }

    companion object {
        fun newInstance() = RecommendationFragment()
    }
}