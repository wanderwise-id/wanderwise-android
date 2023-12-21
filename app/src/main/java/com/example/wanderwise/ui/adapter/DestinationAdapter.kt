package com.example.wanderwise.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.Destination
import com.example.wanderwise.data.database.Score
import com.example.wanderwise.databinding.ListDestinationBinding
import com.example.wanderwise.databinding.ListExploreCityBinding
import com.example.wanderwise.ui.detailcity.DetailInfoCityActivity
import com.example.wanderwise.ui.detailcity.destination.DestinationDetailActivity
import java.io.Serializable

class DestinationAdapter(
    private val destination: ArrayList<Destination>,
    private val city: String
): RecyclerView.Adapter<DestinationAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ListDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val destination = destination[position]
        holder.bind(destination)

        holder.itemView.setOnClickListener {
            Log.d("destination-detail-pressed", "$destination")
            val intent = Intent(holder.itemView.context, DestinationDetailActivity::class.java)
            val data = mapOf<String, String>(
                "DSTN_KEY" to destination.key.toString(),
                "CITY_KEY" to city
            )

            val bundle = Bundle().apply {
                putSerializable("data", data as Serializable)
            }

            intent.putExtra("data", bundle)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return destination.size
    }

    class MyViewHolder(val binding: ListDestinationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(destination: Destination) {
            Glide.with(binding.root)
                .load(destination.image)
                .transform(CenterCrop(), RoundedCorners(40))
                .into(binding.destinationImage)

            binding.destinationName.text = destination.name.toString()
        }
    }
}