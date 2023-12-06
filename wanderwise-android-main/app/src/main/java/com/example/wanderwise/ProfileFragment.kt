\package com.example.wanderwise

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanderwise.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnName.setOnClickListener {
            val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.name_dialog, null)
            val messageBoxBuilder = AlertDialog.Builder(activity).setView(messageBoxView)
            val  messageBoxInstance = messageBoxBuilder.show()
        }

        binding.btnEmail.setOnClickListener {
            val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.email_dialog, null)
            val messageBoxBuilder = AlertDialog.Builder(activity).setView(messageBoxView)
            val  messageBoxInstance = messageBoxBuilder.show()
        }

        binding.btnSetting.setOnClickListener {
            val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.settings_dialog, null)
            val messageBoxBuilder = AlertDialog.Builder(activity).setView(messageBoxView)
            val  messageBoxInstance = messageBoxBuilder.show()
        }

        binding.btnFeedback.setOnClickListener {
            val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.feedback_dialog, null)
            val messageBoxBuilder = AlertDialog.Builder(activity).setView(messageBoxView)
            val  messageBoxInstance = messageBoxBuilder.show()
        }

        binding.btnAboutus.setOnClickListener {
            val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.aboutus_dialog, null)
            val messageBoxBuilder = AlertDialog.Builder(activity).setView(messageBoxView)
            val  messageBoxInstance = messageBoxBuilder.show()
        }

        return view
    }

}