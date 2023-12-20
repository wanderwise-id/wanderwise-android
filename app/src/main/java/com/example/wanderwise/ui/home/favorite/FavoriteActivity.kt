package com.example.wanderwise.ui.home.favorite

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.DataNeed
import com.example.wanderwise.data.database.Information
import com.example.wanderwise.data.database.Score
import com.example.wanderwise.data.database.ScoreCurrent
import com.example.wanderwise.data.database.Weather
import com.example.wanderwise.data.local.database.CityFavorite
import com.example.wanderwise.data.preferences.UserModel
import com.example.wanderwise.databinding.ActivityFavoriteBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.adapter.CityExploreAdapter
import com.example.wanderwise.ui.adapter.DetailListCityAdapter
import com.example.wanderwise.ui.home.HomeViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class FavoriteActivity : AppCompatActivity() {

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var detailListCityAdapter: DetailListCityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db =
            FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")


        homeViewModel.getAllCity().observe(this@FavoriteActivity) { cityLove ->
            cityLove.forEach() {
                val cities = ArrayList<DataNeed>()
                if (cityLove != null) {
                    db.getReference("scores/${it.key}").limitToLast(1).get()
                        .addOnSuccessListener { score ->
                            DataNeed(
                                score.getValue<DataNeed>()!!.score
                            )
                        }

                    db.getReference("informations/${it.key}}").get()
                        .addOnSuccessListener { info ->
                            info.children.map { infor ->
                                cities.add(
                                    DataNeed(
                                        infor.key,
                                        infor.getValue<DataNeed>()!!.numberOfDestination,
                                        infor.getValue<DataNeed>()!!.numberOfHospitals,
                                        infor.getValue<DataNeed>()!!.numberOfPoliceStations
                                    )
                                )
                            }
                        }

                    val refWeathers = db.getReference("weathers/${it.key}}")
                    val weatherListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.childrenCount > 0) {
                                cities.add(
                                    DataNeed(
                                        dataSnapshot.getValue<DataNeed>()!!.weather,
                                        dataSnapshot.getValue<DataNeed>()!!.temperature
                                    )
                                )
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                        }
                    }
                    refWeathers.addValueEventListener(weatherListener)
                    detailListCityAdapter = DetailListCityAdapter(this, cities)
                    binding.rvCityLove.layoutManager =
                        LinearLayoutManager(this)
                    binding.rvCityLove.setHasFixedSize(true)
                    binding.rvCityLove.adapter = detailListCityAdapter
                }
            }
        }

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        supportActionBar?.title = "Favorite city"
    }

}
