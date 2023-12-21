package com.example.wanderwise.ui.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mystoryapp.utils.reduceFileImage
import com.example.mystoryapp.utils.uriToFile
import com.example.wanderwise.data.Result
import com.example.wanderwise.data.preferences.UserModel
import com.example.wanderwise.ui.profile.smallmenu.AboutUsFragment
import com.example.wanderwise.ui.profile.smallmenu.EmailChangeFragment
import com.example.wanderwise.ui.login.LoginScreenActivity
import com.example.wanderwise.ui.profile.smallmenu.NameChangeFragment
import com.example.wanderwise.ui.profile.smallmenu.SendFeedbackFragment
import com.example.wanderwise.ui.profile.smallmenu.SettingsChangeFragment
import com.example.wanderwise.databinding.FragmentProfileBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.post.addpost.AddPostViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.internal.notify


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var imagePickUri: Uri? = null

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imagePickUri = uri
            showImage()
        } else {
            Log.d("Error Pick", "No Media Selected")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        imagePickUri?.let { uri ->
            binding.profileImage.setImageURI(uri)
            val image = uriToFile(uri, requireActivity()).reduceFileImage()

            profileViewModel.uploadPhotoProfile(image).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            isLoading(true)
                        }

                        is Result.Success -> {
                            showToast(result.data)
                            isLoading(false)
                        }

                        is Result.Error -> {
                            showToast(result.error)
                            isLoading(false)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.profileImage.setOnClickListener {
            startGallery()
        }

        profileViewModel.isLoading.observe(viewLifecycleOwner) {
            isLoading(it)
        }

        profileViewModel.snackbar.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { SnackBarText ->
                Snackbar.make(
                    view,
                    SnackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        profileViewModel.getPhoto().observe(viewLifecycleOwner) {
            Glide.with(requireContext())
                .load(it.body.photo)
                .into(binding.profileImage)
        }

        profileViewModel.getSessionUser().observe(viewLifecycleOwner) {
            binding.usernameProfile.text = it.name
            binding.emailUserProfile.text = it.email
        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun isLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }


    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }
}