package com.example.wanderwise.ui.post.addpost

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.wanderwise.databinding.ActivityAddPostBinding
import com.example.wanderwise.ui.post.PostFragment

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
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
        imagePickUri.let {
            binding.pickImageButton.setImageURI(it)
        }
    }

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

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        supportActionBar?.title = "Add post"
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