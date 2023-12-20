package com.example.wanderwise.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wanderwise.ui.home.emergency.HospitalAndPoliceFragment
import com.example.wanderwise.ui.post.AllAndYourPostFragment

class EmergencyAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = HospitalAndPoliceFragment()
        fragment.arguments = Bundle().apply {
            putInt(HospitalAndPoliceFragment.ARG_POSITION, position + 1)
        }
        return fragment
    }

}