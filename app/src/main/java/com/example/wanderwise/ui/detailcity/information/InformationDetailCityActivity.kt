package com.example.wanderwise.ui.detailcity.information

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wanderwise.R

class InformationDetailCityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_detail_city)

        supportActionBar?.hide()
    }
}