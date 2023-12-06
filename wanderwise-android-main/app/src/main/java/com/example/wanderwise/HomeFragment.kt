package com.example.wanderwise

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.wanderwise.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.favoriteButton.setOnClickListener {
            val intentFavorite = Intent(activity, FavoriteActivity::class.java)
            startActivity(intentFavorite)
        }

        binding.notificationButton.setOnClickListener {
            val intentNotif = Intent(activity, NotificationActivity::class.java)
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
            val intentDetailInfo = Intent(activity, DetailInfoCityActivity::class.java)
            startActivity(intentDetailInfo)
        }

        binding.seeAll.setOnClickListener {
            val fragmentManager = parentFragmentManager

            fragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, PostFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}