package com.example.wanderwise.ui.home.emergency

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wanderwise.R
import com.example.wanderwise.data.database.Destination
import com.example.wanderwise.data.database.HospitalPolice
import com.example.wanderwise.data.response.CreatedAt
import com.example.wanderwise.data.response.PostsItem
import com.example.wanderwise.databinding.FragmentHospitalAndPoliceBinding
import com.example.wanderwise.databinding.FragmentPostBinding
import com.example.wanderwise.ui.adapter.DestinationAdapter
import com.example.wanderwise.ui.adapter.EmergencyAdapter
import com.example.wanderwise.ui.adapter.EmergencyCallsAdapter
import com.example.wanderwise.ui.adapter.PostAdapter
import com.example.wanderwise.ui.post.AllAndYourPostFragment
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.firestore

class HospitalAndPoliceFragment : Fragment() {

    private var position: Int? = null
    private var _binding: FragmentHospitalAndPoliceBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterUserPost: EmergencyCallsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHospitalAndPoliceBinding.inflate(inflater, container, false)
        val view = binding.root

        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")

        arguments?.let {
            position = it.getInt(ARG_POSITION)
        }

        val cityKey = "Denpasar"

        if (position == 1) {
            binding.headlineList.text = getString(R.string.hospital_list)

            showLoading(true)
            val ref = db.getReference("emergency_calls/${cityKey}/hospital")
            val hospital = ArrayList<HospitalPolice>()
            val hospitalListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.map {
                        hospital.add(
                            HospitalPolice(
                                it.key,
                                it.getValue<HospitalPolice>()!!.address,
                                it.getValue<HospitalPolice>()!!.name,
                                it.getValue<HospitalPolice>()!!.telp
                            )
                        )
                    }

                    showLoading(false)
                    adapterUserPost = EmergencyCallsAdapter(hospital)
                    binding.rvHospitalPolice.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvHospitalPolice.setHasFixedSize(true)
                    binding.rvHospitalPolice.adapter = adapterUserPost

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }
            }
            ref.addValueEventListener(hospitalListener)

        } else {
            binding.headlineList.text = getString(R.string.police_list)

            showLoading(true)
            val ref = db.getReference("emergency_calls/${cityKey}/police")
            val police = ArrayList<HospitalPolice>()
            val policeListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.map {
                        police.add(
                            HospitalPolice(
                                it.key,
                                it.getValue<HospitalPolice>()!!.address,
                                it.getValue<HospitalPolice>()!!.name,
                                it.getValue<HospitalPolice>()!!.telp
                            )
                        )
                    }

                    showLoading(false)
                    adapterUserPost = EmergencyCallsAdapter(police)
                    binding.rvHospitalPolice.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvHospitalPolice.setHasFixedSize(true)
                    binding.rvHospitalPolice.adapter = adapterUserPost

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                }
            }
            ref.addValueEventListener(policeListener)
        }

        return view
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
    }
}