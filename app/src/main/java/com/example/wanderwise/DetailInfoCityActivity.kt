package com.example.wanderwise

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.wanderwise.databinding.ActivityDetailInfoCityBinding
import com.google.android.material.tabs.TabLayout

class DetailInfoCityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailInfoCityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInfoCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when (tab.position) {
                        0 -> {

                        }
                        1 -> {

                        }
                        2 -> {
                            val intentNews = Intent(this@DetailInfoCityActivity, NewsDetailCategoryActivity::class.java)
                            startActivity(intentNews)
                        }
                        3 -> {
                            val intentInfo = Intent(this@DetailInfoCityActivity, InformationDetailCityActivity::class.java)
                            startActivity(intentInfo)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        supportActionBar?.title = "Detail info city"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}