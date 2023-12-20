package com.example.wanderwise.ui.detailcity

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.Destination
import com.example.wanderwise.data.database.FullInfo
import com.example.wanderwise.data.database.Information
import com.example.wanderwise.data.database.LocationCity
import com.example.wanderwise.data.database.Score
import com.example.wanderwise.databinding.FragmentDestinationBinding
import com.example.wanderwise.databinding.FragmentInformationBinding
import com.example.wanderwise.databinding.FragmentNewsBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.adapter.DestinationAdapter
import com.example.wanderwise.ui.home.HomeViewModel
import com.example.wanderwise.utils.MyLocation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.cos

class InformationFragment : Fragment() {

    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        val view = binding.root

        val cityKey = (requireActivity().application as MyLocation).sharedData

        Log.d("information-city-key","$cityKey")

        if (cityKey == null){
            return view
        }

        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")

        var city = City(
            "",
            1,
            "",
            "",
            "",
            "",
            LocationCity(
                "",
                ""
            )
        )

        try {
            lifecycleScope.launch {
                val citySnapshot = db.getReference("cities/${cityKey}").get().await()
                if (citySnapshot.hasChildren()) {
                    city = City(
                        citySnapshot.key,
                        citySnapshot.getValue<City>()!!.area,
                        citySnapshot.getValue<City>()!!.capital,
                        citySnapshot.getValue<City>()!!.country,
                        citySnapshot.getValue<City>()!!.description,
                        citySnapshot.getValue<City>()!!.image,
                        citySnapshot.getValue<City>()!!.location,
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("Error", "Failed to fetch data: ${e.message}")
        }

        var score: Any? = null
        try {
            lifecycleScope.launch {
                val scoreSnapshot = db.getReference("scores/${cityKey}").limitToLast(1).get().await()
                if (scoreSnapshot.childrenCount > 0) {
                    for (child in scoreSnapshot.children){
                        score = child.getValue<Score>()!!.score
                    }
                } else {
                    score = 0
                }
            }
        } catch (e: Exception) {
            Log.e("Error", "Failed to fetch data: ${e.message}")
        }

        try {
            lifecycleScope.launch {
                val informationSnapshot = db.getReference("informations/${cityKey}").limitToLast(1).get().await()
                var information = FullInfo()

                if (informationSnapshot.hasChildren()){
                    informationSnapshot.children.forEach{child ->
                        Log.d("informationsDebugChild", "$child")
                        information = FullInfo(
                            key = child.key,
                            costOfLife = child.getValue<FullInfo>()!!.costOfLife,
                            internet = child.getValue<FullInfo>()!!.internet,
                            temperature = child.getValue<FullInfo>()!!.temperature,
                            numberOfDestinations = child.getValue<FullInfo>()!!.numberOfDestinations,
                            numberOfHospitals = child.getValue<FullInfo>()!!.numberOfHospitals,
                            numberOfPoliceStations = child.getValue<FullInfo>()!!.numberOfPoliceStations,
                            population = child.getValue<FullInfo>()!!.population
                        )
                    }
                }

                var population = 0

                if (information.population != null){
                    population = (information.population.toString().toDouble()/city.area.toString().toDouble()).toInt()
                }

                binding.populationAmount.text = "${population.toString()} ppl/km²"
                binding.costAmount.text = "${information.costOfLife.toString()} USD/Month"

                var internetSpeed = ""

                if (information.internet != null){
                    information.internet = information.internet.toString().toInt()

                    if (information.internet as Int <= 20)
                        internetSpeed = "Slow"

                    if (information.internet as Int >= 21 && information.internet as Int <= 40)
                        internetSpeed = "Medium"

                    if (information.internet as Int > 40)
                        internetSpeed = "Fast"
                }

                binding.internetSpeed.text = "$internetSpeed ${information.internet} Mbps (avg)"
                binding.temperatureAmount.text = "${information.temperature} °C"
                binding.hospitalAmountInfo.text = "${information.numberOfHospitals} Hospital"
                binding.policeAmountInfo.text = "${information.numberOfPoliceStations} Police"

                if (score != null){
                    if (score.toString().toDouble() <= 33) {
                        binding.safetyScoreLevel.text = getString(R.string.danger)
                        binding.safetyImage.setImageResource(R.drawable.danger_icon_large)
                    } else if (score.toString().toDouble() <= 70) {
                        binding.safetyScoreLevel.text = getString(R.string.warning)
                        binding.safetyImage.setImageResource(R.drawable.warning_icon_large)
                    } else if (score.toString().toDouble() <= 100) {
                        binding.safetyScoreLevel.text = getString(R.string.safe)
                        binding.safetyImage.setImageResource(R.drawable.safe_icon_large)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Error", "Failed to fetch data: ${e.message}")
        }

        return view
    }
}