package com.example.wanderwise.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.Score
import com.example.wanderwise.data.local.database.CityFavorite
import com.example.wanderwise.databinding.ListExploreCityBinding
import com.example.wanderwise.ui.detailcity.DetailInfoCityActivity
import com.example.wanderwise.ui.home.HomeViewModel


class CityExploreAdapter(
    private val context: Context,
    private val cities: ArrayList<City>,
    private val scores: ArrayList<Double>,
    private val homeViewModel: HomeViewModel,
    private val cityFavorite: CityFavorite,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<CityExploreAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ListExploreCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val city = cities[position]
        val score = scores[position]

        holder.bind(context, score, city, homeViewModel, cityFavorite, viewLifecycleOwner)

        holder.itemView.setOnClickListener {


            val intent = Intent(holder.itemView.context, DetailInfoCityActivity::class.java)
            intent.putExtra(DetailInfoCityActivity.CITY, city.key.toString())
            holder.itemView.context.startActivity(intent)
        }

        holder.binding.loveIcon.setOnClickListener {
            cityFavorite.let {
                it.key = city.key.toString()
                it.isLoved = true
            }

            homeViewModel.insert(cityFavorite)
        }
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    class MyViewHolder(val binding: ListExploreCityBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(context: Context, score:Double, city: City, homeViewModel: HomeViewModel, cityFavorite: CityFavorite, viewLifecycleOwner: LifecycleOwner) {
            homeViewModel.getClickedCity(city.key.toString()).observe(viewLifecycleOwner) { clickedCity ->
                if (clickedCity != null && clickedCity.key == city.key.toString()) {
                    binding.loveIcon.setImageDrawable(ContextCompat.getDrawable(binding.loveIcon.context, R.drawable.love_fill_icon))
                } else {
                    binding.loveIcon.setImageDrawable(ContextCompat.getDrawable(binding.loveIcon.context, R.drawable.love_outline_icon))
                }
            }

            Glide.with(binding.root)
                .load(city.image)
                .into(binding.imagePreview)

            binding.cityName.text = city.key.toString()

            val scoreCity = score
            Log.d("TestingScoreCityAdapter", "${city.key.toString()} $scoreCity")
            if (scoreCity.toString().toDouble() <= 100) {
                binding.safetyLevel.text = context.getString(R.string.safe)
                binding.iconSafetyMedium.setImageResource(R.drawable.safe_icon_medium)
            }
            if (scoreCity.toString().toDouble() <= 70) {
                binding.safetyLevel.text = context.getString(R.string.warning)
                binding.iconSafetyMedium.setImageResource(R.drawable.warning_icon_medium)
            }
            if (scoreCity.toString().toDouble() <= 33) {
                binding.safetyLevel.text = context.getString(R.string.danger)
                binding.iconSafetyMedium.setImageResource(R.drawable.danger_icon_medium)
            }
        }
    }
}
