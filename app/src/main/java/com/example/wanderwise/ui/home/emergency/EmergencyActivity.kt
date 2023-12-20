package com.example.wanderwise.ui.home.emergency

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.example.wanderwise.R
import com.example.wanderwise.databinding.ActivityEmergencyBinding
import com.example.wanderwise.ui.adapter.EmergencyAdapter
import com.example.wanderwise.ui.adapter.PostPagerAdapter
import com.example.wanderwise.ui.post.PostFragment
import com.google.android.material.tabs.TabLayoutMediator

class EmergencyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmergencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagerAdapter = EmergencyAdapter(this)
        val viewAdapter: ViewPager2 = binding.viewPager
        viewAdapter.adapter = pagerAdapter

        TabLayoutMediator(binding.tabs, viewAdapter) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        supportActionBar?.title = "Emergency call in your city"
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.hospital,
            R.string.police
        )
    }
}