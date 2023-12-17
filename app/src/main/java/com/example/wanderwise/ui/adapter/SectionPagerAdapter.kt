package com.example.wanderwise.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wanderwise.ui.detailcity.DestinationFragment
import com.example.wanderwise.ui.detailcity.InformationFragment
import com.example.wanderwise.ui.detailcity.MitigationFragment
import com.example.wanderwise.ui.detailcity.NewsFragment

class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var cityKey: String = ""

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DestinationFragment()
            1 -> NewsFragment()
            2 -> MitigationFragment()
            3 -> InformationFragment()

            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Destination"
            1 -> "News"
            2 -> "Mitigation"
            3 -> "Information"

            else -> null
        }
    }

}