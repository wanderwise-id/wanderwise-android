package com.example.wanderwise.ui.home.morecity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.DataNeed
import com.example.wanderwise.data.database.Information
import com.example.wanderwise.data.database.Score
import com.example.wanderwise.data.database.Weather
import com.example.wanderwise.data.local.database.CityFavorite
import com.example.wanderwise.databinding.ActivityDetailInfoCityBinding
import com.example.wanderwise.databinding.ActivityExploreCityMoreBinding
import com.example.wanderwise.databinding.ActivityFavoriteBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.adapter.DetailListCityAdapter
import com.example.wanderwise.ui.home.HomeViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ExploreCityMoreActivity : AppCompatActivity() {
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityExploreCityMoreBinding

    private lateinit var detailListCityAdapter: DetailListCityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreCityMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")

        var cities:MutableMap<String, DataNeed> = mutableMapOf()
        try {
            lifecycleScope.launch {
                val citiesSnapshot = db.getReference("cities").get().await()
                Log.d("jagoan neon : cities", "$citiesSnapshot")
                if (citiesSnapshot.childrenCount > 0){
                    citiesSnapshot.children.forEach { city ->
                        if (city.exists()){
                            cities.put(
                                city.key.toString(),
                                DataNeed(
                                    key = city.key,
                                    image = city.getValue<City>()!!.image.toString(),
                                    area = city.getValue<City>()!!.area
                                )
                            )
                        }

                        val informationSnapshot = db.getReference("informations/${city.key}").limitToLast(1).get().await()
                        Log.d("jagoan neon : information", "$informationSnapshot")
                        if (informationSnapshot.hasChildren()){
                            informationSnapshot.children.forEach{information ->
                                cities[city.key]?.numberOfDestination = information.getValue<Information>()!!.numberOfDestinations
                                cities[city.key]?.numberOfHospitals = information.getValue<Information>()!!.numberOfHospitals
                                cities[city.key]?.numberOfPoliceStations = information.getValue<Information>()!!.numberOfPoliceStations
                            }
                        }

                        val weatherSnapshot = db.getReference("weathers/${city.key}").get().await()
                        Log.d("jagoan neon : weather", "$weatherSnapshot")
                        if (weatherSnapshot.exists()){
                            cities[city.key]?.weather = weatherSnapshot.getValue<Weather>()!!.weather
                            cities[city.key]?.temperature = weatherSnapshot.getValue<Weather>()!!.temperature
                        }

                        val scoreSnapshot = db.getReference("scores/${city.key}").limitToLast(1).get().await()
                        Log.d("jagoan neon : scores", "$scoreSnapshot")
                        if (scoreSnapshot.hasChildren()){
                            scoreSnapshot.children.forEach{
                                cities[city.key]?.score = it.getValue<Score>()!!.score
                            }
                        }
                    }
                }

                val cityFavorite: CityFavorite = CityFavorite(
                    id = 0,
                    key = "",
                    isLoved = false
                )

                detailListCityAdapter = DetailListCityAdapter(homeViewModel,this@ExploreCityMoreActivity, cities, cityFavorite)
                binding.rvExplore.layoutManager = LinearLayoutManager(this@ExploreCityMoreActivity)
                binding.rvExplore.setHasFixedSize(true)
                binding.rvExplore.adapter = detailListCityAdapter
            }
        }  catch (e: Exception){
            Log.e("Error", "Failed to fetch data: ${e.message}")
        }


        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        supportActionBar?.title = "Explore city"
    }
}