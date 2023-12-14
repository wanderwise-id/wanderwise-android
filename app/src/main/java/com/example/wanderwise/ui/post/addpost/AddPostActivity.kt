package com.example.wanderwise.ui.post.addpost

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import com.example.mystoryapp.utils.reduceFileImage
import com.example.mystoryapp.utils.uriToFile
import com.example.wanderwise.R
import com.example.wanderwise.databinding.ActivityAddPostBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.data.Result
import com.example.wanderwise.data.response.UploadPostData
import com.google.android.material.snackbar.Snackbar

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
            onBackPressed()
        }

        binding.uploadButton.setOnClickListener {
            imagePickUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                val name = binding.captionEdit.text.toString()
                val price = binding.cityEdit.text.toString().toInt()

                Log.d("TestingNamePrice"," $name dan $price dan $imageFile")

                postViewModel.uploadImage(imageFile, name, price).observe(this) {
                    showToast("Success Upload")
                }
            }
        }

        postViewModel.snackbar.observe(this) {
            it.getContentIfNotHandled()?.let { SnackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    SnackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

//        binding.uploadButton.setOnClickListener {
//            imagePickUri?.let { uri ->
//                val name = binding.captionEdit.text.toString().trim()
//                val cityString = binding.cityEdit.text.toString().trim()
//
//                if (name.isNotEmpty() && cityString.isNotEmpty()) {
//                    val city = cityString.toInt()
//                    val image = uriToFile(uri, this).reduceFileImage()
//
//                    Log.d("ImageUri", "$image")
//
//                    postViewModel.uploadPost(name, city, image).observe(this) { result ->
//                        if (result != null) {
//                            when (result) {
//                                is Result.Loading -> {
//                                    // Handle loading state if needed
//                                }
//
//                                is Result.Success -> {
//                                    showToast("Post uploaded successfully")
//                                    // Handle success state if needed
//                                }
//
//                                is Result.Error -> {
//                                    showToast("Error uploading post: ${result.error}")
//                                    // Handle error state if needed
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        supportActionBar?.title = "Add post"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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