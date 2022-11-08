package com.example.movies.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movies.R
import java.util.*
import kotlin.concurrent.schedule

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        Timer().schedule(2000) {
            setResult(RESULT_OK, Intent());
            finish()
        }
    }
}