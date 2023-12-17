package com.example.wanderwise.ui.detailcity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wanderwise.R
import com.example.wanderwise.data.database.Destination
import com.example.wanderwise.data.database.FullInfo
import com.example.wanderwise.data.database.Information
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

        val refInformation = db.getReference("informations")
        val informations = ArrayList<FullInfo>()
        val infoListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.children.map {
                    informations.add(
                        FullInfo(
                            it.key,
                            it.getValue<FullInfo>()!!.costOfLife,
                            it.getValue<FullInfo>()!!.internet,
                            it.getValue<FullInfo>()!!.numberOfDestinations,
                            it.getValue<FullInfo>()!!.numberOfHospitals,
                            it.getValue<FullInfo>()!!.numberOfPoliceStations,
                            it.getValue<FullInfo>()!!.population
                        )
                    )
                }

                informations.forEach() {
                    if (it.key == cityKey) {
                        val costOfLife = it.costOfLife.toString()
                        val formattedCost = getString(R.string._900rb_month, costOfLife)
                        binding.costAmount.text = formattedCost

                        if (scoreLevel.toString() <= 33.toString()) {
                            binding.safetyScoreLevel.text = getString(R.string.danger)
                            binding.safetyImage.setImageResource(R.drawable.danger_icon_large)
                        } else if (scoreLevel.toString() <= 70.toString()) {
                            binding.safetyScoreLevel.text = getString(R.string.warning)
                            binding.safetyImage.setImageResource(R.drawable.warning_icon_large)
                        } else if (scoreLevel.toString() <= 100.toString()) {
                            binding.safetyScoreLevel.text = getString(R.string.safe)
                            binding.safetyImage.setImageResource(R.drawable.safe_icon_large)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        refInformation.addValueEventListener(infoListener)

        return view
    }
}