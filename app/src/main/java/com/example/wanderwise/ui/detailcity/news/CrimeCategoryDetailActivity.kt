package com.example.wanderwise.ui.detailcity.news

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.Crime
import com.example.wanderwise.data.database.Image
import com.example.wanderwise.data.database.LocationCrime
import com.example.wanderwise.data.database.News
import com.example.wanderwise.data.database.Score
import com.example.wanderwise.databinding.ActivityCrimeCategoryDetailBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.adapter.CityExploreAdapter
import com.example.wanderwise.ui.adapter.CrimeCategoryNewsAdapter
import com.example.wanderwise.ui.home.HomeViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class CrimeCategoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrimeCategoryDetailBinding

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var crimeNewsAdapter: CrimeCategoryNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrimeCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    val refCrime = db.getReference("news/${it.key}/Crime")
                    val newsDetails = ArrayList<Crime>()
                    val newsAmountListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.children.map {dataSnapshot ->
                                newsDetails.add(
                                    Crime(
                                        dataSnapshot.key,
                                        dataSnapshot.getValue<Crime>()!!.category,
                                        dataSnapshot.getValue<Crime>()!!.date_published,
                                        dataSnapshot.getValue<Crime>()!!.image,
                                        dataSnapshot.getValue<Crime>()!!.location,
                                        dataSnapshot.getValue<Crime>()!!.summarize,
                                        dataSnapshot.getValue<Crime>()!!.timezone,
                                        dataSnapshot.getValue<Crime>()!!.title
                                    )
                                )

                                crimeNewsAdapter = CrimeCategoryNewsAdapter(this@CrimeCategoryDetailActivity, newsDetails)
                                binding.rvCrimeCategoryNews.layoutManager = LinearLayoutManager(this@CrimeCategoryDetailActivity)
                                binding.rvCrimeCategoryNews.setHasFixedSize(true)
                                binding.rvCrimeCategoryNews.adapter = crimeNewsAdapter
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                        }
                    }
                    refCrime.addValueEventListener(newsAmountListener)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(cityListener)

        supportActionBar?.hide()
    }
}