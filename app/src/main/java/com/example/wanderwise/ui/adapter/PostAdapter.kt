package com.example.wanderwise.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.Score
import com.example.wanderwise.data.response.CreatedAt
import com.example.wanderwise.data.response.GetAllPostResponse
import com.example.wanderwise.data.response.PostsItem
import com.example.wanderwise.databinding.ListExploreCityBinding
import com.example.wanderwise.databinding.ListPostPageBinding
import com.example.wanderwise.ui.detailcity.DetailInfoCityActivity
import java.util.Date
import java.util.Locale

class PostAdapter(
    private val context: Context,
    private val userPost: ArrayList<PostsItem>
) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ListPostPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = userPost[position]

        holder.bind(context, user)

    }

    override fun getItemCount(): Int {
        return userPost.size
    }

    class MyViewHolder(val binding: ListPostPageBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(context: Context, postUser: PostsItem) {

            Glide.with(binding.root)
                .load(postUser.image)
                .into(binding.postImagePreview)

            binding.usernameUserPost.text = postUser.name.toString()

            binding.descriptionPost.text = postUser.caption.toString()

            binding.locationNamePost.text = postUser.title.toString()

            Glide.with(binding.root)
                .load(postUser.photoUser)
                .transform(CenterCrop(), RoundedCorners(100))
                .into(binding.userImage)

            val date = Date((postUser.createdAt.seconds.toString().toDouble() * 1000 + postUser.createdAt.nanoseconds.toString().toDouble() / 1000000).toLong())

            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

            val formattedDate: String = sdf.format(date)
            binding.datePost.text = formattedDate
        }
    }
}