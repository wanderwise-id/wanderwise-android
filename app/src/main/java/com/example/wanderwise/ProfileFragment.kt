package com.example.wanderwise

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.wanderwise.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.usernameEdit.setOnClickListener {
            val dialogFragment = NameChangeFragment()
            dialogFragment.show(parentFragmentManager, "NameChangeDialog")
        }

        binding.emailEdit.setOnClickListener {
            val dialogFragment = EmailChangeFragment()
            dialogFragment.show(parentFragmentManager, "EmailChangeDialog")
        }

        binding.settings.setOnClickListener {
            val dialogFragment = SettingsChangeFragment()
            dialogFragment.show(parentFragmentManager, "SettingsDialog")
        }

        binding.feedback.setOnClickListener {
            val dialogFragment = SendFeedbackFragment()
            dialogFragment.show(parentFragmentManager, "FeedbackDialog")
        }

        binding.aboutUs.setOnClickListener {
            val dialogFragment = AboutUsFragment()
            dialogFragment.show(parentFragmentManager, "AboutUsDialog")
        }

        binding.logOut.setOnClickListener {
            val intentLogOut = Intent(activity, LoginScreenActivity::class.java)
            startActivity(intentLogOut)
        }

        return view
    }
}