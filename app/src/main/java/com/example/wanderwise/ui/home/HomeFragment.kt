package com.example.wanderwise.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.firestore

class HomeFragment : Fragment() {

    private lateinit var cityAdapter: CityExploreAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var postAdapter: PostHomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        (requireActivity().application as MyLocation).sharedData = "Alex"

        var currentLoc = ""

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
                        binding.locationName.text = "Your Location not Reached"
                        binding.cityImage.setImageResource(R.drawable.baseline_warning_image)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }
            }
            ref.addValueEventListener(cityListener)
        }

        val cities = ArrayList<City>()
        val scores: MutableMap<String, Score> = mutableMapOf()
        db.getReference("cities").get().addOnSuccessListener {
            it.children.map {city ->
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

            cities.forEach {city ->
                db.getReference("scores/${city.key}").limitToLast(1).get().addOnSuccessListener {score ->
                    if (!score.exists()) {
                        scores[score.key.toString()] = Score()
                    } else {
                        scores[score.key.toString()] = Score(
                            score.key,
                            score.getValue<Score>()!!.score
                        )
                    }
                }
            }

            cityAdapter = CityExploreAdapter(requireActivity(), cities, scores)
            binding.exploreCityRv.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            binding.exploreCityRv.setHasFixedSize(true)
            binding.exploreCityRv.adapter = cityAdapter
        }

        val dbPost = Firebase.firestore
        val userAllPosts = ArrayList<PostsItem>()

        dbPost.collection("posts")
            .get()
            .addOnSuccessListener {
                it.documents.forEach { doc ->

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

            val intent = Intent(activity, DetailInfoCityActivity::class.java)
            intent.putExtra(DetailInfoCityActivity.KEY_CITY, currentLoc)
            startActivity(intent)
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