package com.example.wanderwise.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wanderwise.ui.post.AllAndYourPostFragment

class PostPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = AllAndYourPostFragment()
        fragment.arguments = Bundle().apply {
            putInt(AllAndYourPostFragment.ARG_POSITION, position + 1)
        }
        return fragment
    }

}