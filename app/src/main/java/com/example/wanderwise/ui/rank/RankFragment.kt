package com.example.wanderwise.ui.rank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanderwise.databinding.FragmentPostBinding
import com.example.wanderwise.databinding.FragmentRankBinding

class RankFragment : Fragment() {
    private var _binding: FragmentRankBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRankBinding.inflate(inflater, container, false)
        val view = binding.root



        return view

    }

}