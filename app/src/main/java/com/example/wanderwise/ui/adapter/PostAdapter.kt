package com.example.wanderwise.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wanderwise.data.response.GetAllPostResponse
import com.example.wanderwise.databinding.ListPostPageBinding

class PostAdapter(private val activity: Activity) : ListAdapter<GetAllPostResponse, PostAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListPostPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val allUserPost = getItem(position)
        holder.bind(allUserPost)
    }

    class MyViewHolder(val binding: ListPostPageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(allUserPost: GetAllPostResponse){
            allUserPost.body.posts.forEach() {
                binding.datePost.text = it.createdAt.seconds.toString()
                binding.descriptionPost.text = it.caption
                Glide.with(binding.root)
                    .load(it.image)
                    .into(binding.postImagePreview)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<GetAllPostResponse>(){
            override fun areItemsTheSame(
                oldItem: GetAllPostResponse,
                newItem: GetAllPostResponse
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: GetAllPostResponse,
                newItem: GetAllPostResponse
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}