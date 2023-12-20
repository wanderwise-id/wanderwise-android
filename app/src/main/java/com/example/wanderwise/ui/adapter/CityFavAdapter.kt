//package com.example.wanderwise.ui.adapter
//
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.wanderwise.data.database.City
//import com.example.wanderwise.databinding.ListCityMoreDetailBinding
//
//class CityFavAdapter() : RecyclerView.Adapter<CityFavAdapter.MyViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val binding = ListCityMoreDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MyViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.bind(listUser[position])
//    }
//
//    override fun getItemCount(): Int {
//        return listUser.size
//    }
//
//    private val listUser = ArrayList<City>()
//
//    fun setListUser(listUserFav: List<UserFavorite>) {
//        val diffCallback = FavUserDiffCallback(this.listUser, listUserFav)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        this.listUser.clear()
//        this.listUser.addAll(listUserFav)
//        diffResult.dispatchUpdatesTo(this)
//    }
//
//    class MyViewHolder(val binding: ListCityMoreDetailBinding): RecyclerView.ViewHolder (binding.root) {
//        fun bind(user: UserFavorite) {
//            binding.usernameGithub.text = user.usernameFav
//            Glide.with(binding.root)
//                .load(user.photo)
//                .into(binding.profileImage)
//
//            binding.cardViewItem.setOnClickListener {
//                val intent = Intent(it.context, DetailUser::class.java)
//                intent.putExtra(USER_NAME, user.usernameFav)
//                it.context.startActivity(intent)
//            }
//
//            binding.shareButton.setOnClickListener {
//                val sendIntent: Intent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_TEXT, "Username GitHub: ${user.usernameFav},\nhttps://github.com/${user.usernameFav}")
//                    type = "text/plain"
//                }
//
//                val shareIntent = Intent.createChooser(sendIntent, null)
//                it.context.startActivity(shareIntent)
//            }
//        }
//    }
//}