package com.example.movies.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.movies.R
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.activity_filter.view.*


class FilterActivity : AppCompatActivity() {

    private val filterList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)


        buttonOK.setOnClickListener {
            filterList.add(spinnerGenres.selectedItem.toString())
            filterList.add(spinnerCountry.selectedItem.toString())
            if (minRating.text.toString().isEmpty()) filterList.add("0")
            else filterList.add(minRating.text.toString())
            if (maxRating.text.toString().isEmpty()) filterList.add("10")
            else filterList.add(maxRating.text.toString())
            if (minYear.text.toString().isEmpty()) filterList.add("2012")
            else filterList.add(minYear.text.toString())
            if (maxYear.text.toString().isEmpty()) filterList.add("3000")
            else filterList.add(maxYear.text.toString())
            filterList.add(spinnerType.selectedItem.toString())
            val intent = Intent()
            intent.putExtra("filterList", filterList)
            setResult(RESULT_OK, intent)
            finish()
        }

        buttonCancel.setOnClickListener {
            Log.i("resultCode", "RESULT_CANCELED")
            setResult(RESULT_CANCELED)
            finish()
        }

        clearBtn.setOnClickListener {
            setResult(RESULT_FIRST_USER)
            finish()
        }
    }
}