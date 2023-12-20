package com.example.wanderwise.ui.detailcity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.databinding.ActivityDetailInfoCityBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.adapter.SectionPagerAdapter
import com.example.wanderwise.ui.home.HomeViewModel
import com.example.wanderwise.utils.MyLocation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class DetailInfoCityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailInfoCityBinding
    private var exploreCard: String? = null
    private var detailCard: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInfoCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        exploreCard = intent.getStringExtra(CITY)
        detailCard = intent.getStringExtra(KEY_CITY)

        val cityKey = exploreCard ?: detailCard

        (this.application as MyLocation).sharedData = cityKey.toString()

        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")

        db.getReference("cities/$cityKey").get().addOnSuccessListener {
            if (it.exists()) {
                val titleCity = it.key.toString()

                val formattedTitle = getString(R.string.what_is_denpasar_city, titleCity)
                binding.titleCity.text = formattedTitle
                Glide.with(binding.root)
                    .load(it.getValue<City>()!!.image.toString())
                    .into(binding.cityPhoto)
                binding.descriptionSummary.text = it.getValue<City>()!!.description.toString()
            }
        }

        val pagerAdapter = SectionPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
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

    companion object {
        const val CITY = "city"
        const val KEY_CITY = "key"
    }
}