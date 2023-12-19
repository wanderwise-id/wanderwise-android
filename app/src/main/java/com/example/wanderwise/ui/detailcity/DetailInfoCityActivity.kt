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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class DetailInfoCityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailInfoCityBinding
    private var cityKey: String? = null
    private var keyCity: String? = null
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInfoCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cityKey = intent.getStringExtra(CITY)

        keyCity = intent.getStringExtra(KEY_CITY)

        homeViewModel.locationData = cityKey

        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")

        val ref = db.getReference("cities")
        val cities = ArrayList<City>()
        val cityListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.children.map {
                    cities.add(
                        City(
                            it.key,
                            it.getValue<City>()!!.area,
                            it.getValue<City>()!!.capital,
                            it.getValue<City>()!!.country,
                            it.getValue<City>()!!.description,
                            it.getValue<City>()!!.image,
                            it.getValue<City>()!!.location
                        )
                    )
                }

                cities.forEach() {
                    if (it.key == keyCity) {
                        val titleCity = it.key.toString()

                        val formattedTitle = getString(R.string.what_is_denpasar_city, titleCity)
                        binding.titleCity.text = formattedTitle
                        Glide.with(binding.root)
                            .load(it.image)
                            .into(binding.cityPhoto)
                        binding.descriptionSummary.text = it.description.toString()
                    } else if (it.key == cityKey) {
                        val titleCity = it.key.toString()

                        val formattedTitle = getString(R.string.what_is_denpasar_city, titleCity)
                        binding.titleCity.text = formattedTitle
                        Glide.with(binding.root)
                            .load(it.image)
                            .into(binding.cityPhoto)
                        binding.descriptionSummary.text = it.description.toString()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(cityListener)

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