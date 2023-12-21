package com.example.wanderwise.ui.profile.smallmenu

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.wanderwise.R
import com.example.wanderwise.data.database.Feedback
import com.example.wanderwise.databinding.FragmentProfileBinding
import com.example.wanderwise.databinding.FragmentSendFeedbackBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.home.HomeViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class SendFeedbackFragment : DialogFragment() {
    private var _binding: FragmentSendFeedbackBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSendFeedbackBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.feedbackEdit.text
        binding.cancelButton.setOnClickListener{
            dismiss()
        }

        binding.uploadButton.setOnClickListener{
            var feedback: HashMap<String, Any>? = hashMapOf()

            homeViewModel.getSessionUser().observe(viewLifecycleOwner) { user ->
                feedback = hashMapOf(
                    "message" to binding.feedbackEdit.text.toString(), // Convert to List
                    "email" to user.email,
                    "uid" to user.uid
                )
            }

            if (feedback?.isNotEmpty() == true) {
                val db = Firebase.firestore
                db.collection("feedback").add(feedback!!).addOnSuccessListener {
                    Toast.makeText(context, "Success Send Feedback", Toast.LENGTH_LONG).show()
                    dismiss()
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed Send Feedback", Toast.LENGTH_LONG).show()
                }
            }
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}