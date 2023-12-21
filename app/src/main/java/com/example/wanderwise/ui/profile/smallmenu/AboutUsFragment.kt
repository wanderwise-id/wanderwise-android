package com.example.wanderwise.ui.profile.smallmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.wanderwise.R
import com.example.wanderwise.databinding.FragmentAboutUsBinding
import com.example.wanderwise.databinding.FragmentNameChangeBinding

class AboutUsFragment : DialogFragment() {
    private var _binding: FragmentAboutUsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.closeFragment.setOnClickListener {
            dismiss()
        }

        return view
    }
}