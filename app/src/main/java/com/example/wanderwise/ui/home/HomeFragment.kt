package com.example.wanderwise.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.wanderwise.ui.detailcity.DetailInfoCityActivity
import com.example.wanderwise.ui.home.emergency.EmergencyActivity
import com.example.wanderwise.ui.home.morecity.ExploreCityMoreActivity
import com.example.wanderwise.ui.home.favorite.FavoriteActivity
import com.example.wanderwise.ui.home.notification.NotificationActivity
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.Information
import com.example.wanderwise.data.database.Score
import com.example.wanderwise.data.database.ScoreCurrent
import com.example.wanderwise.data.database.Weather
import com.example.wanderwise.data.local.database.CityFavorite
import com.example.wanderwise.data.preferences.UserModel
import com.example.wanderwise.data.response.CreatedAt
import com.example.wanderwise.data.response.PostsItem
import com.example.wanderwise.databinding.FragmentHomeBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.adapter.CityExploreAdapter
import com.example.wanderwise.ui.adapter.PostHomeAdapter
import com.example.wanderwise.utils.MyLocation
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var cityAdapter: CityExploreAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var postAdapter: PostHomeAdapter

    private val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        (requireActivity().application as MyLocation).sharedData = "Alex"

        var currentLoc = ""
        var disableDetailAccess = false

        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")

        homeViewModel.getSessionUser().observe(viewLifecycleOwner) { cityUser ->

            homeViewModel.editUserModel(UserModel(
                name = cityUser.name,
                token = cityUser.token,
                email = cityUser.email,
                uid = cityUser.uid,
                userLocation = cityUser.userLocation,
                currentActivity = "profile",
                isLogin = true
            ))

            currentLoc = cityUser.userLocation

            val ref = db.getReference("cities/${currentLoc}")
            val cityListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        binding.locationName.text = dataSnapshot.key.toString()
                        Glide.with(requireActivity())
                            .load(dataSnapshot.getValue<City>()!!.image.toString())
                            .transform(CenterCrop(), RoundedCorners(40))
                            .into(binding.cityImage)
                    } else {
                        binding.locationName.text = "Unlisted"
                        binding.cityImage.setImageResource(R.drawable.baseline_warning_image)
                        disableDetailAccess = true
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }
            }
            ref.addValueEventListener(cityListener)

            val currentTime = System.currentTimeMillis()
            val oneDayAgo = currentTime - (24 * 60 * 60 * 1000)
            val refNotifications = db.getReference("notifications/${currentLoc}").orderByChild("timestamp").startAt(oneDayAgo.toString().toDouble())
            val notificationListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.notifAmount.text = snapshot.childrenCount.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", error.toException())
                }
            }
            refNotifications.addValueEventListener(notificationListener)

            val refWeathers = db.getReference("weathers/${currentLoc}")
            val weatherListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.childrenCount > 0){
                        val temperature = dataSnapshot.getValue<Weather>()!!.temperature.toString()
                        val formattedTemperature = getString(R.string._29_c, temperature)
                        binding.temperature.text = formattedTemperature

                        when (dataSnapshot.getValue<Weather>()!!.weather.toString()) {
                            "rain" -> {
                                binding.weatherIcon.setImageResource(R.drawable.rainy)
                            }
                            "sunny" -> {
                                binding.weatherIcon.setImageResource(R.drawable.sunny)
                            }
                            "stormy" -> {
                                binding.weatherIcon.setImageResource(R.drawable.stormy)
                            }
                            "cloudy" -> {
                                binding.weatherIcon.setImageResource(R.drawable.cloudy)
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }
            }
            refWeathers.addValueEventListener(weatherListener)

            val refScores = db.getReference("scores/$currentLoc").limitToLast(1)
            var scoreCurrent: Any? = null
            val scoreCurrentListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.childrenCount > 0) {
                        dataSnapshot.children.map {
                            scoreCurrent = it.getValue<ScoreCurrent>()!!.score
                        }
                    } else {
                        scoreCurrent = 0
                    }

                    if (scoreCurrent.toString().toDouble() <= 33) {
                        binding.safetyLevelText.text = getString(R.string.danger)
                        binding.safetyIcon.setImageResource(R.drawable.danger_icon_small)
                    } else if (scoreCurrent.toString().toDouble() <= 70) {
                        binding.safetyLevelText.text = getString(R.string.warning)
                        binding.safetyIcon.setImageResource(R.drawable.warning_icon_small)
                    } else if (scoreCurrent.toString().toDouble() <= 100) {
                        binding.safetyLevelText.text = getString(R.string.safe)
                        binding.safetyIcon.setImageResource(R.drawable.safe_icon_small)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }
            }
            refScores.addValueEventListener(scoreCurrentListener)

            val refInformation = db.getReference("informations/${currentLoc}").limitToLast(1)
            val infoListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){
                        dataSnapshot.children.map {
                            binding.destinationsAmount.text = it.getValue<Information>()!!.numberOfDestinations.toString()
                            binding.hospitalAmount.text = it.getValue<Information>()!!.numberOfHospitals.toString()
                            binding.policeAmount.text = it.getValue<Information>()!!.numberOfPoliceStations.toString()
                        }
                    } else {
                        binding.destinationsAmount.text = "0"
                        binding.hospitalAmount.text = "0"
                        binding.policeAmount.text = "0"
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }
            }
            refInformation.addValueEventListener(infoListener)
        }

        try {

            val cities = ArrayList<City>()
            val scores = ArrayList<Double>()
            var citiesSnapshot: Any? = null
            lifecycleScope.launch {
                citiesSnapshot = db.getReference("cities").get().await()

                (citiesSnapshot as DataSnapshot?)?.children?.forEach { city ->
                    cities.add(
                        City(
                            city.key,
                            city.getValue<City>()!!.area,
                            city.getValue<City>()!!.capital,
                            city.getValue<City>()!!.country,
                            city.getValue<City>()!!.description,
                            city.getValue<City>()!!.image,
                            city.getValue<City>()!!.location
                        )
                    )
                }

                val deferredScores = cities.map { city ->
                    async {
                        val scoreSnapshot = db.getReference("scores/${city.key}").limitToLast(1).get().await()

                        if (scoreSnapshot.childrenCount > 0) {
                            scoreSnapshot.children.first().getValue<Score>()?.score?.toString()?.toDouble() ?: 0.0
                        } else {
                            0.0
                        }
                    }
                }

                val cityFavorite: CityFavorite = CityFavorite(
                    id = 0,
                    key = "",
                    isLoved = false
                )

                scores.addAll(deferredScores.map { it.await() })

                cityAdapter = CityExploreAdapter(requireActivity(), cities, scores, homeViewModel, cityFavorite, viewLifecycleOwner)
                binding.exploreCityRv.layoutManager =
                    LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                binding.exploreCityRv.setHasFixedSize(true)
                binding.exploreCityRv.adapter = cityAdapter
            }
        } catch (e: Exception){
            Log.e("Error", "Failed to fetch data: ${e.message}")
        }


        try {
            val dbPost = Firebase.firestore
            val userAllPosts = ArrayList<PostsItem>()

            lifecycleScope.launch {
                val postsDataSnapshot = dbPost.collection("posts").get().await()

                postsDataSnapshot.documents.forEach{doc ->
                    userAllPosts.add(
                        PostsItem(
                            image = doc.getString("image"),
                            createdAt = CreatedAt(
                                seconds = doc.getTimestamp("createdAt")!!.seconds,
                                nanoseconds = doc.getTimestamp("createdAt")!!.nanoseconds
                            ),
                            caption = doc.getString("caption"),
                            id = doc.getString("userId"),
                            title = doc.getString("city"),
                            idPost =  doc.getString("idPost"),
                            name = doc.getString("name")
                        )
                    )
                }

                postAdapter = PostHomeAdapter(requireActivity(), userAllPosts)
                binding.popularPostRv.layoutManager = LinearLayoutManager(requireActivity(),  LinearLayoutManager.HORIZONTAL, false)
                binding.popularPostRv.setHasFixedSize(true)
                binding.popularPostRv.adapter = postAdapter
            }
        } catch (e: Exception){
            Log.e("Error", "Failed to fetch data: ${e.message}")
        }

        binding.favoriteButton.setOnClickListener {
            val intentFavorite = Intent(activity, FavoriteActivity::class.java)
            startActivity(intentFavorite)
        }

        binding.notificationButton.setOnClickListener {
            val intentNotif = Intent(activity, NotificationActivity::class.java)
            intentNotif.putExtra("cityKey", currentLoc)
            startActivity(intentNotif)
        }

        binding.seeDetailButton.setOnClickListener {
            val intentExplore = Intent(activity, ExploreCityMoreActivity::class.java)
            startActivity(intentExplore)
        }

        binding.emergencyButton.setOnClickListener {
            val intentEmergency = Intent(activity, EmergencyActivity::class.java)
            startActivity(intentEmergency)
        }

        binding.cardDetailCity.setOnClickListener {
            homeViewModel.getSessionUser().observe(viewLifecycleOwner) { user ->
                homeViewModel.editUserModel(
                    UserModel(
                        name = user.name,
                        token = user.token,
                        email = user.email,
                        uid = user.uid,
                        userLocation = user.userLocation,
                        currentActivity = "CardCurrentLocation",
                        isLogin = true
                    )
                )
            }

            if(!disableDetailAccess){
                val intent = Intent(activity, DetailInfoCityActivity::class.java)
                intent.putExtra(DetailInfoCityActivity.KEY_CITY, currentLoc)
                startActivity(intent)
            } else {
                Toast.makeText(context, "Your Location is Not Listed in Our Database", Toast.LENGTH_LONG).show()
            }

        }

        binding.seeAll.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)
            findNavController().navigate(R.id.postFragment)
        }

        homeViewModel.getSessionUser().observe(viewLifecycleOwner) {
            binding.usernameUser.text = it.name
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}