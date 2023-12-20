package com.example.wanderwise.ui.detailcity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


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

        try {
            lifecycleScope.launch{
                val citySnapshot = db.getReference("cities/$cityKey").get().await()

                if (citySnapshot.exists()) {
                    val titleCity = citySnapshot.key.toString()

                    val formattedTitle = getString(R.string.what_is_denpasar_city, titleCity)
                    binding.titleCity.text = formattedTitle
                    Glide.with(binding.root)
                        .load(citySnapshot.getValue<City>()!!.image.toString())
                        .into(binding.cityPhoto)
                    binding.descriptionSummary.text = citySnapshot.getValue<City>()!!.description.toString()
                }

                val pagerAdapter = SectionPagerAdapter(supportFragmentManager)
                binding.viewPager.adapter = pagerAdapter
                binding.tabs.setupWithViewPager(binding.viewPager)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
                supportActionBar?.title = "Detail info city"
            }
        } catch (e: Exception) {
            Log.e("Error", "Failed to fetch data: ${e.message}")
        }
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