package com.example.wanderwise.ui.detailcity

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.Destination
import com.example.wanderwise.data.database.FullInfo
import com.example.wanderwise.data.database.Information
import com.example.wanderwise.data.database.Score
import com.example.wanderwise.databinding.FragmentDestinationBinding
import com.example.wanderwise.databinding.FragmentInformationBinding
import com.example.wanderwise.databinding.FragmentNewsBinding
import com.example.wanderwise.ui.adapter.DestinationAdapter
import com.example.wanderwise.ui.home.HomeViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlin.math.cos

class InformationFragment : Fragment() {

    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        val view = binding.root

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        val cityKey = homeViewModel.locationData
        val scoreLevel = homeViewModel.safetyScore

        Log.d("IsiSafetyScore", "$scoreLevel")
        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")

        val refCity = db.getReference("cities/${cityKey}")
        var city = City()
        val cityListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                city = City(
                    dataSnapshot.key,
                    dataSnapshot.getValue<City>()!!.area,
                    dataSnapshot.getValue<City>()!!.capital,
                    dataSnapshot.getValue<City>()!!.country,
                    dataSnapshot.getValue<City>()!!.description,
                    dataSnapshot.getValue<City>()!!.image,
                    dataSnapshot.getValue<City>()!!.location,
                )
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        refCity.addValueEventListener(cityListener)

        var score:Long = 0
        val refScore = db.getReference("scores/${cityKey}").limitToLast(1)
        val scoreListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.childrenCount <= 0) {
                    for (child in dataSnapshot.children){
                        score = child.getValue<Score>()!!.score as Long
                    }
                } else {
                    score = 0
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        refScore.addValueEventListener(scoreListener)

        val refInformation = db.getReference("informations/${cityKey}").limitToLast(1)
        val infoListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var information = FullInfo()

                if (dataSnapshot.childrenCount <= 0){
                    information = FullInfo(
                        "",
                        0,
                        0, 0,
                        0,
                        0,
                        0,
                        0)
                } else {
                    for (child in dataSnapshot.children){
                        information = FullInfo(
                            child.key,
                            child.getValue<FullInfo>()!!.costOfLife,
                            child.getValue<FullInfo>()!!.internet,
                            child.getValue<FullInfo>()!!.temperature,
                            child.getValue<FullInfo>()!!.numberOfDestinations,
                            child.getValue<FullInfo>()!!.numberOfHospitals,
                            child.getValue<FullInfo>()!!.numberOfPoliceStations,
                            child.getValue<FullInfo>()!!.population
                        )
                    }
                }

                if (information != null){
                    val population = (information.population.toString().toDouble()/city.area.toString().toDouble()).toInt()

                    binding.populationAmount.text = "${population.toString()} ppl/km²"
                    binding.costAmount.text = "${information.costOfLife.toString()} USD/Month"

                    var internetSpeed = ""
                    information.internet = information.internet.toString().toInt()

                    if (information.internet as Int <= 20)
                        internetSpeed = "Slow"

                    if (information.internet as Int >= 21 && information.internet as Int <= 40)
                        internetSpeed = "Medium"

                    if (information.internet as Int > 40)
                        internetSpeed = "Fast"

                    binding.internetSpeed.text = "${internetSpeed} ${information.internet} Mbps (avg)"
                    binding.temperatureAmount.text = "${information.temperature} °C"
                    binding.hospitalAmountInfo.text = "${information.numberOfHospitals} Hospital"
                    binding.policeAmountInfo.text = "${information.numberOfPoliceStations} Police"
                }

                if (score != null){
                    if (score <= 33) {
                        binding.safetyScoreLevel.text = "Danger"
                        binding.safetyImage.setImageResource(R.drawable.danger_icon_large)
                    } else if (score <= 70) {
                        binding.safetyScoreLevel.text = "Warning"
                        binding.safetyImage.setImageResource(R.drawable.warning_icon_large)
                    } else if (score <= 100) {
                        binding.safetyScoreLevel.text = "Safe"
                        binding.safetyImage.setImageResource(R.drawable.safe_icon_large)
                    }
                }
                Log.d("score", "${score}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        refInformation.addValueEventListener(infoListener)

        return view
    }
}