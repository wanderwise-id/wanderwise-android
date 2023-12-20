package com.example.wanderwise.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.DataNeed
import com.example.wanderwise.data.database.Destination
import com.example.wanderwise.data.database.Information
import com.example.wanderwise.data.database.Score
import com.example.wanderwise.data.database.Weather
import com.example.wanderwise.databinding.ListCityMoreDetailBinding
import com.example.wanderwise.databinding.ListDestinationBinding
import com.google.firebase.database.getValue

class DetailListCityAdapter(
    private val context: Context,
    private val city: ArrayList<DataNeed>
): RecyclerView.Adapter<DetailListCityAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            ListCityMoreDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (city.isNotEmpty() && position < city.size) {
            val cities = city[position]
            holder.bind(context, cities)
        }
    }

    override fun getItemCount(): Int {
        return city.size
    }

    class MyViewHolder(val binding: ListCityMoreDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, city: DataNeed) {

            Glide.with(binding.root)
                .load(city.image)
                .transform(CenterCrop(), RoundedCorners(40))
                .into(binding.cityImage)

            binding.currentLocText.text = city.area.toString()

            binding.locationName.text = city.key.toString()

            binding.destinationsAmount.text = city.numberOfDestination.toString()

            binding.hospitalAmount.text = city.numberOfHospitals.toString()

            binding.policeAmount.text = city.numberOfPoliceStations.toString()

            when (city.weather.toString()) {
                "rain" -> {
                    binding.weatherIcon.setImageResource(R.drawable.rainy)
                }
                "sunny" -> {
                    binding.weatherIcon.setImageResource(R.drawable.sunny)
                }
                "stormy" -> {
                    binding.weatherIcon.setImageResource(R.drawable.stormy)
                }
                "cloudy" -> {
                    binding.weatherIcon.setImageResource(R.drawable.cloudy)
                }
            }
            if (city.score != null) {
                if (city.score.toString().toDouble() <= 33) {
                    binding.safetyLevelText.text = context.getString(R.string.danger)
                    binding.safetyIcon.setImageResource(R.drawable.danger_icon_small)
                } else if (city.score.toString().toDouble() <= 70) {
                    binding.safetyLevelText.text = context.getString(R.string.warning)
                    binding.safetyIcon.setImageResource(R.drawable.warning_icon_small)
                } else if (city.score.toString().toDouble() <= 100) {
                    binding.safetyLevelText.text = context.getString(R.string.safe)
                    binding.safetyIcon.setImageResource(R.drawable.safe_icon_small)
                }

            }
        }
    }
}