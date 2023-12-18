package com.example.wanderwise.ui.post

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.wanderwise.data.response.GetAllPostResponse
import com.example.wanderwise.ui.post.addpost.AddPostActivity
import com.example.wanderwise.databinding.FragmentPostBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.adapter.PostAdapter
import com.example.wanderwise.ui.adapter.PostPagerAdapter
import com.example.wanderwise.ui.home.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator

class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val view = binding.root

        val pagerAdapter = PostPagerAdapter(requireActivity())
        val viewAdapter: ViewPager2 = binding.viewPager
        viewAdapter.adapter = pagerAdapter

        binding.addPost.setOnClickListener {
            val intentAdd = Intent(activity, AddPostActivity::class.java)
            startActivity(intentAdd)
        }

        return view
    }


}