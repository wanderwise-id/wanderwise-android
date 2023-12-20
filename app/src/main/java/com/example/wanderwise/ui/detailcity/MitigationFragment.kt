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
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.Mitigation
import com.example.wanderwise.data.database.Score
import com.example.wanderwise.databinding.FragmentInformationBinding
import com.example.wanderwise.databinding.FragmentMitigationBinding
import com.example.wanderwise.ui.adapter.DestinationAdapter
import com.example.wanderwise.ui.home.HomeViewModel
import com.example.wanderwise.utils.MyLocation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class MitigationFragment : Fragment() {

    private var _binding: FragmentMitigationBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMitigationBinding.inflate(inflater, container, false)
        val view = binding.root

        val cityKey = (requireActivity().application as MyLocation).sharedData

        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")

        db.getReference("mitigations/${cityKey}").get().addOnSuccessListener {
            if(it.childrenCount > 0){
                binding.mitigationInfo.text = it.getValue<Mitigation>()!!.content.toString()
                binding.mitigationHead.text = it.getValue<Mitigation>()!!.head.toString()
            } else{
                binding.mitigationInfo.text = "Tidak Ada Informasi Mitigasi"
                binding.mitigationHead.text = "~"
            }
        }

        return view
    }
}