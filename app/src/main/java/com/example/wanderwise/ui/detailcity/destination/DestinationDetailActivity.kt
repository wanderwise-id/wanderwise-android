package com.example.wanderwise.ui.detailcity.destination

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.wanderwise.R
import com.example.wanderwise.data.database.Destination
import com.example.wanderwise.databinding.ActivityCrimeCategoryDetailBinding
import com.example.wanderwise.databinding.ActivityDestinationDetailBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DestinationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDestinationDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDestinationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data_key = intent.getBundleExtra("data")
        val receivedMap = data_key?.getSerializable("data") as? Map<String, String>
        val city_key = receivedMap?.get("CITY_KEY")
        val destination_key = receivedMap?.get("DSTN_KEY")

        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")
        try {
            lifecycleScope.launch {
                val destinationSnapshot = db.getReference("destinations/$city_key").child(
                    destination_key.toString()
                ).get().await()
                Log.d("destination-detail-object", "$destinationSnapshot")
                if (destinationSnapshot.exists()){
                    var title = "Undefined Place"
                    var description = "Undefined Description"

                    if (destinationSnapshot.getValue<Destination>()!!.name != null){
                        title = destinationSnapshot.getValue<Destination>()!!.name.toString()
                    }

                    if (destinationSnapshot.getValue<Destination>()!!.description != null){
                        description = destinationSnapshot.getValue<Destination>()!!.description.toString()
                    }

                    binding.destinationNameTitle.text = title
                    binding.destinationDescription.text = description

                    if (destinationSnapshot.getValue<Destination>()!!.image != null){
                        Glide.with(binding.root)
                            .load(destinationSnapshot.getValue<Destination>()!!.image.toString())
                            .transform(CenterCrop(), RoundedCorners(20))
                            .into(binding.destinationImagePreview)
                    } else {
                        binding.destinationImagePreview.setImageResource(R.drawable.baseline_warning_image)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Error", "Failed to fetch data: ${e.message}")
        }

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        supportActionBar?.title = "Destination"
    }
}