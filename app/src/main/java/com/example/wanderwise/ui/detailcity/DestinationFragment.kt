package com.example.wanderwise.ui.detailcity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.Destination
import com.example.wanderwise.databinding.FragmentDestinationBinding
import com.example.wanderwise.databinding.FragmentHomeBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.adapter.CityExploreAdapter
import com.example.wanderwise.ui.adapter.DestinationAdapter
import com.example.wanderwise.ui.home.HomeViewModel
import com.example.wanderwise.utils.MyLocation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class DestinationFragment : Fragment() {

    private lateinit var cityAdapter: DestinationAdapter

    private var _binding: FragmentDestinationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDestinationBinding.inflate(inflater, container, false)
        val view = binding.root

        val cityKey = (requireActivity().application as MyLocation).sharedData

        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")

        val ref = db.getReference("destinations/${cityKey}")
        val destinations = ArrayList<Destination>()
        val destinationListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.map {
                    destinations.add(
                        Destination(
                            it.key,
                            it.getValue<Destination>()!!.image,
                            it.getValue<Destination>()!!.location,
                            it.getValue<Destination>()!!.name
                        )
                    )
                }

                destinations.forEach() { _ ->
                    cityAdapter = DestinationAdapter(destinations)
                        binding.rvDestination.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvDestination.setHasFixedSize(true)
                        binding.rvDestination.adapter = cityAdapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(destinationListener)

        return view
    }

    companion object {
        const val CITY = "city"
    }
}