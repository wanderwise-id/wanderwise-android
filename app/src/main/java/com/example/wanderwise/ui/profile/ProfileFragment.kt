package com.example.wanderwise.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.wanderwise.ui.profile.smallmenu.AboutUsFragment
import com.example.wanderwise.ui.profile.smallmenu.EmailChangeFragment
import com.example.wanderwise.ui.login.LoginScreenActivity
import com.example.wanderwise.ui.profile.smallmenu.NameChangeFragment
import com.example.wanderwise.ui.profile.smallmenu.SendFeedbackFragment
import com.example.wanderwise.ui.profile.smallmenu.SettingsChangeFragment
import com.example.wanderwise.databinding.FragmentProfileBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.post.addpost.AddPostViewModel


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

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
            profileViewModel.logoutUser()
            val intentLogOut = Intent(activity, LoginScreenActivity::class.java)
            intentLogOut.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentLogOut)
        }

        return view
    }
}