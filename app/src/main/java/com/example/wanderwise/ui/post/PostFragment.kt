package com.example.wanderwise.ui.post

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanderwise.ui.post.addpost.AddPostActivity
import com.example.wanderwise.databinding.FragmentPostBinding

class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.addPost.setOnClickListener {
            val intentAdd = Intent(activity, AddPostActivity::class.java)
            startActivity(intentAdd)
        }

        return view
    }
}