package com.example.wanderwise.ui.detailcity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val ref = db.getReference("news/${cityKey}")
        val newsAmount = ArrayList<News>()
        val newsAmountListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.map {
                    newsAmount.add(
                        News(
                            it.key
                        )
                    )
                }

                if (newsAmount.size > 0) {
                    Glide.with(requireActivity())
                        .load(R.drawable.crime_image)
                        .transform(CenterCrop(), RoundedCorners(40))
                        .into(binding.imagePreviewNews)

                    binding.notifAmount.text = newsAmount.size.toString()

                    binding.crimeCard.setOnClickListener {
                        val intentCrimeNews = Intent(activity, CrimeCategoryDetailActivity::class.java)
                        startActivity(intentCrimeNews)
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(newsAmountListener)

        return view
    }

}