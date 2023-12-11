package com.example.wanderwise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment

class AboutUsFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view?.findViewById<ImageView>(R.id.close_fragment)?.setOnClickListener {
            dismissAllowingStateLoss();
        }

        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }
}