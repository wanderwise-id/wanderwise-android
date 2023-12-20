package com.example.wanderwise.ui.detailcity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.wanderwise.R
import com.example.wanderwise.data.database.Destination
import com.example.wanderwise.data.database.News
import com.example.wanderwise.databinding.FragmentDestinationBinding
import com.example.wanderwise.databinding.FragmentNewsBinding
import com.example.wanderwise.ui.adapter.DestinationAdapter
import com.example.wanderwise.ui.detailcity.news.CrimeCategoryDetailActivity
import com.example.wanderwise.ui.home.HomeViewModel
import com.example.wanderwise.utils.MyLocation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val view = binding.root

        val cityKey = (requireActivity().application as MyLocation).sharedData
        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")

        val newsAmount = ArrayList<News>()
        db.getReference("news/${cityKey}").get().addOnSuccessListener{dataSnapshot ->
            dataSnapshot.children.map {category ->
                category.children.map {news ->
                    newsAmount.add(
                        News(
                            news.key
                        )
                    )
                }
            }

            var newsSize = 0
            if (newsAmount.size > 0) {
                newsSize = newsAmount.size
            }

            Glide.with(requireActivity())
                .load(R.drawable.crime_image)
                .transform(CenterCrop(), RoundedCorners(40))
                .into(binding.imagePreviewNews)

            binding.notifAmount.text = newsSize.toString()

            binding.crimeCard.setOnClickListener {
                if (newsSize > 0){
                    val intentCrimeNews = Intent(activity, CrimeCategoryDetailActivity::class.java)
                    intentCrimeNews.putExtra(CrimeCategoryDetailActivity.CITY, cityKey.toString())
                    startActivity(intentCrimeNews)
                } else {
                    Toast.makeText(context, "$cityKey doesn't have any Crime News", Toast.LENGTH_LONG).show()
                }
            }
        }

        return view
    }

}