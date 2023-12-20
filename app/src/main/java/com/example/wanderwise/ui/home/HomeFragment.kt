package com.example.wanderwise.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.example.wanderwise.ui.adapter.PostAdapter
import com.example.wanderwise.ui.adapter.PostHomeAdapter
import com.example.wanderwise.ui.detailcity.DestinationFragment
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

                    val scoreLast: MutableMap<String, Score> = mutableMapOf()

                    cities.forEach() {
                        if (it.key == currentLoc) {
                            binding.locationName.text = it.key.toString()
                            Glide.with(requireActivity())
                                .load(it.image)
                                .transform(CenterCrop(), RoundedCorners(40))
                                .into(binding.cityImage)
                        }

                        val cityScoreRef = db.getReference("scores/${it.key}")
                        val scores = ArrayList<Score>()
                        val scoreListener = object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                dataSnapshot.children.map { dataSnapshotScore ->
                                    scores.add(
                                        Score(
                                            dataSnapshotScore.key,
                                            dataSnapshotScore.getValue<Score>()!!.dateTime,
                                            dataSnapshotScore.getValue<Score>()!!.description,
                                            dataSnapshotScore.getValue<Score>()!!.score
                                        )
                                    )
                                }
                                scoreLast[it.key.toString()] = if (scores.isNotEmpty()) scores[scores.size - 1] else Score()

                                cityAdapter = CityExploreAdapter(requireActivity(), cities, scoreLast)
                                binding.exploreCityRv.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                                binding.exploreCityRv.setHasFixedSize(true)
                                binding.exploreCityRv.adapter = cityAdapter
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                            }
                        }
                        cityScoreRef.addValueEventListener(scoreListener)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }
            }
            ref.addValueEventListener(cityListener)

            val refInformation = db.getReference("informations/${currentLoc}").limitToLast(1)
            val informations = ArrayList<Information>()
            val infoListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    dataSnapshot.children.map {
                        informations.add(
                            Information(
                                it.key,
                                it.getValue<Information>()!!.costOfLife,
                                it.getValue<Information>()!!.internet,
                                it.getValue<Information>()!!.numberOfDestinations,
                                it.getValue<Information>()!!.numberOfHospitals,
                                it.getValue<Information>()!!.numberOfPoliceStations,
                                it.getValue<Information>()!!.population
                            )
                        )
                    }

                    informations.forEach() {
                        binding.destinationsAmount.text = it.numberOfDestinations.toString()
                        binding.hospitalAmount.text = it.numberOfHospitals.toString()
                        binding.policeAmount.text = it.numberOfPoliceStations.toString()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }
            }
            refInformation.addValueEventListener(infoListener)

            val refWeathers = db.getReference("weathers")
            val weathers = ArrayList<Weather>()
            val weatherListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    dataSnapshot.children.map {
                        weathers.add(
                            Weather(
                                it.key,
                                it.getValue<Weather>()!!.dateTime,
                                it.getValue<Weather>()!!.temperature,
                                it.getValue<Weather>()!!.weather
                            )
                        )
                    }

                    weathers.forEach() {
                        if (it.key == currentLoc) {
                            val temperature = it.temperature.toString()
                            val formattedTemperature = getString(R.string._29_c, temperature)
                            binding.temperature.text = formattedTemperature

                            if (it.weather == "rain") {
                                binding.weatherIcon.setImageResource(R.drawable.rainy)
                            } else if (it.weather == "sunny") {
                                binding.weatherIcon.setImageResource(R.drawable.sunny)
                            } else if (it.weather == "stormy") {
                                binding.weatherIcon.setImageResource(R.drawable.stormy)
                            } else if (it.weather == "cloudy") {
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

            Log.d("IsScoreCurrent", currentLoc)
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
                    Log.d("IsScoreCurrent", "$scoreCurrent")

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

            val dbPost = Firebase.firestore
            val userAllPosts = ArrayList<PostsItem>()

            dbPost.collection("posts")
                .get()
                .addOnSuccessListener {
                    it.documents.forEach() { doc ->

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