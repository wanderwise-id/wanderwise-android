package com.example.wanderwise.ui.post.addpost

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.FragmentTransaction
import com.example.mystoryapp.utils.reduceFileImage
import com.example.mystoryapp.utils.uriToFile
import com.example.wanderwise.R
import com.example.wanderwise.databinding.ActivityAddPostBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.data.Result
import com.example.wanderwise.ui.post.PostFragment
import com.example.wanderwise.ui.profile.ProfileFragment

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private var imagePickUri: Uri? = null

    private val postViewModel by viewModels<AddPostViewModel> {
        ViewModelFactory.getInstance(this)
    }

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
        imagePickUri.let {
            binding.pickImageButton.setImageURI(it)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pickCityButton.setOnClickListener {
            val intentMaps = Intent(this, MapsActivity::class.java)
            startActivity(intentMaps)
        }

        binding.pickImageButton.setOnClickListener {
            startGallery()
        }

        binding.cancelButton.setOnClickListener {
            binding.captionEdit.text = null
            binding.cityEdit.text = null
            binding.pickImageButton.setImageURI(null)
            onBackPressed()
        }

        binding.uploadButton.setOnClickListener {
            imagePickUri?.let { uri ->
                val caption = binding.captionEdit.text.toString().trim()
                val city = binding.cityEdit.text.toString().trim()
                val imageFile = uriToFile(uri, this).reduceFileImage()

                postViewModel.uploadImage(city, caption, imageFile).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                isLoading(true)
                            }

                            is Result.Success -> {
                                showToast(result.data)
                                isLoading(false)

                                binding.captionEdit.text = null
                                binding.cityEdit.text = null
                                binding.pickImageButton.setImageURI(null)
                                onBackPressed()
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

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        supportActionBar?.title = "Add post"
    }

    private fun navigateToYourFragment() {
        // Create an instance of the fragment you want to navigate to
        val newFragment = PostFragment()

        // Start a fragment transaction
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        // Replace the current fragment with the new one
        transaction.replace(R.id.container_fragment, newFragment, "another_fragment_tag")

        // Optionally, add the transaction to the back stack
        transaction.addToBackStack(null)

        // Commit the transaction
        transaction.commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}