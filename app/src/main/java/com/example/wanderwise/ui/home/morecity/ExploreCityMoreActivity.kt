package com.example.wanderwise.ui.home.morecity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wanderwise.R

class ExploreCityMoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore_city_more)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        supportActionBar?.title = "Explore city"
    }
}