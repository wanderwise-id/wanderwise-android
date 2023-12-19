package com.example.wanderwise.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wanderwise.data.database.Crime
import com.example.wanderwise.data.response.PostsItem
import com.example.wanderwise.databinding.ListNewsCategoryBinding
import com.example.wanderwise.databinding.ListPostPageBinding
import java.util.Date
import java.util.Locale

class CrimeCategoryNewsAdapter(
    private val context: Context,
    private val crimeNews: ArrayList<Crime>
) : RecyclerView.Adapter<CrimeCategoryNewsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ListNewsCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = crimeNews[position]

        holder.bind(context, news)

    }

    override fun getItemCount(): Int {
        return crimeNews.size
    }

    class MyViewHolder(val binding: ListNewsCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, crimeNews: Crime) {

            Glide.with(binding.root)
                .load(crimeNews.image.url)
                .into(binding.imageNews)

            binding.titleNews.text = crimeNews.title.toString()

            binding.dateNews.text = crimeNews.date_published.toString()

            binding.descriptionNews.text = crimeNews.summarize.toString()

            binding.readMore.setOnClickListener {

            }
        }
    }
}