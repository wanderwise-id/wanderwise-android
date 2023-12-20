package com.example.wanderwise.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.wanderwise.data.response.PostsItem
import com.example.wanderwise.databinding.ListPopularTravelerPostBinding
import com.example.wanderwise.databinding.ListPostPageBinding
import java.util.Date
import java.util.Locale

class PostHomeAdapter(
    private val context: Context,
    private val userPost: ArrayList<PostsItem>
) : RecyclerView.Adapter<PostHomeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ListPopularTravelerPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = userPost[position]

        holder.bind(context, user)

    }

    override fun getItemCount(): Int {
        return userPost.size
    }

    class MyViewHolder(val binding: ListPopularTravelerPostBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(context: Context, postUser: PostsItem) {

            Glide.with(binding.root)
                .load(postUser.image)
                .transform(CenterCrop(), RoundedCorners(40))
                .into(binding.cityPreviewPost)

            binding.descriptionPopular.text = postUser.caption.toString()

            binding.usernamePopular.text = postUser.name.toString()

            binding.location.text = postUser.title.toString()

            val date = Date((postUser.createdAt.seconds.toString().toDouble() * 1000 + postUser.createdAt.nanoseconds.toString().toDouble() / 1000000).toLong())

            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

            val formattedDate: String = sdf.format(date)
            binding.datePost.text = formattedDate
        }
    }
}