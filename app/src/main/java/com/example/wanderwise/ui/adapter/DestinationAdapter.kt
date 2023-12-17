package com.example.wanderwise.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
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

class DestinationAdapter(
    private val destination: ArrayList<Destination>
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