package com.example.wanderwise.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.wanderwise.R
import com.example.wanderwise.data.database.DataNeed
import com.example.wanderwise.data.local.database.CityFavorite
import com.example.wanderwise.databinding.ListCityMoreDetailBinding
import com.example.wanderwise.ui.detailcity.DetailInfoCityActivity
import com.example.wanderwise.ui.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailListCityAdapter(
    private val homeViewModel: HomeViewModel,
    private val context: Context,
    private val cities: MutableMap<String,DataNeed>,
    private val cityFavorite: CityFavorite
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
        if (cities.isNotEmpty() && position < cities.size) {
            val dataNeedList = cities.values.toList()
            if (position < dataNeedList.size){
                val dataNeed = dataNeedList[position]
                GlobalScope.launch {
                    holder.bind(context, dataNeed, homeViewModel)
                }

                holder.itemView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, DetailInfoCityActivity::class.java)
                    intent.putExtra(DetailInfoCityActivity.CITY, dataNeed.key.toString())
                    holder.itemView.context.startActivity(intent)
                }

                holder.binding.loveIcon.setOnClickListener {
                    GlobalScope.launch(Dispatchers.Default) {
                        val clickedCity = homeViewModel.getClickedCity(dataNeed.key.toString())
                        if (clickedCity != null && clickedCity.key == dataNeed.key.toString()){
                            homeViewModel.delete(clickedCity.key.toString())

                            withContext(Dispatchers.Main){
                                var loveIcon: ImageView = it as ImageView
                                loveIcon.setImageDrawable(ContextCompat.getDrawable(loveIcon.context, R.drawable.love_outline_icon))
                            }
                        } else {
                            cityFavorite.let {
                                it.key = dataNeed.key.toString()
                                it.isLoved = true
                            }

                            homeViewModel.insert(cityFavorite)

                            withContext(Dispatchers.Main){
                                var loveIcon: ImageView = it as ImageView
                                loveIcon.setImageDrawable(ContextCompat.getDrawable(loveIcon.context, R.drawable.love_fill_icon))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    class MyViewHolder(val binding: ListCityMoreDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        suspend fun bind(context: Context, city: DataNeed, homeViewModel: HomeViewModel) {
            GlobalScope.launch(Dispatchers.Default) {
                val clickedCity = homeViewModel.getClickedCity(city.key.toString())
                if (clickedCity != null && clickedCity.key == city.key.toString()) {
                    binding.loveIcon.setImageDrawable(ContextCompat.getDrawable(binding.loveIcon.context, R.drawable.love_fill_icon))
                } else {
                    binding.loveIcon.setImageDrawable(ContextCompat.getDrawable(binding.loveIcon.context, R.drawable.love_outline_icon))
                }
            }

            loadImageAsync(binding.cityImage, city.image.toString())

            binding.currentLocText.text = city.area.toString()

            binding.locationName.text = city.key.toString()

            binding.destinationsAmount.text = city.numberOfDestination.toString()

            binding.hospitalAmount.text = city.numberOfHospitals.toString()

            binding.policeAmount.text = city.numberOfPoliceStations.toString()

            val temperature = city.temperature.toString()
            val formattedTemperature = context.getString(R.string._29_c, temperature)
            binding.temperature.text = formattedTemperature

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

internal fun CoroutineScope.getString(safe: Int): CharSequence? {
    return getString(safe)
}

suspend fun loadImageAsync(imageView: ImageView, imageUrl: String) {
    withContext(Dispatchers.Main) {
        // Load image on the IO dispatcher
        Glide.with(imageView)
            .load(imageUrl)
            .transform(CenterCrop(), RoundedCorners(40))
            .into(imageView)
    }
}
