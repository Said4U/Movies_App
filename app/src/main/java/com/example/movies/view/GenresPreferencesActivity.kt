package com.example.movies.view

import android.graphics.ColorSpace.Model
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.data.GenreData
import com.example.movies.view.adapter.GenreAdapter
import com.example.movies.viewmodel.MoviesActivityViewModel
import kotlinx.android.synthetic.main.activity_genres_preferences.*
import java.util.*
import kotlin.collections.ArrayList


class GenresPreferencesActivity : AppCompatActivity() {

    private val genreList = ArrayList<GenreData>()
    var count = 0
    private val moviesActivityViewModel = MoviesActivityViewModel()
    private val genresChoiceList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genres_preferences)

        val genresAdapter = GenreAdapter(getListData())
        val manager = LinearLayoutManager(this@GenresPreferencesActivity)
        recyclerViewGenres.layoutManager = manager
        recyclerViewGenres.adapter = genresAdapter

        val idPref = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val userID = idPref.getString("userId", "").toString()

        save_genre_btn.setOnClickListener {
            count = 0
            genreList.forEach {
                if (it.isSelected){
                    count++
                    genresChoiceList.add(it.text)
                }
            }
            if (count != 3){
                Toast.makeText(this, "Необходимо выбрать 3 жанра", Toast.LENGTH_SHORT).show()
            }else{
                moviesActivityViewModel.writeGenresPreferences(userID, genresChoiceList.toList())
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun getListData(): ArrayList<GenreData> {
        val genreListName = listOf(
            "триллер",
            "драма",
            "криминал",
            "мелодрама",
            "детектив",
            "фантастика",
            "приключения",
            "биография",
            "фильм-нуар",
            "вестерн",
            "боевик",
            "фэнтези",
            "комедия",
            "военный",
            "история",
            "музыка",
            "ужасы",
            "мультфильм",
            "семейный",
            "мюзикл",
            "спорт",
            "документальный",
            "короткометражка",
            "аниме",
            "новости",
            "концерт",
            "для взрослых",
            "церемония",
            "реальное ТВ",
            "игра",
            "ток-шоу",
            "детский",
        )
        genreListName.forEach {
            genreList.add(GenreData(it.replaceFirstChar { genre -> genre.uppercase() }))

        }
        return genreList
    }
}