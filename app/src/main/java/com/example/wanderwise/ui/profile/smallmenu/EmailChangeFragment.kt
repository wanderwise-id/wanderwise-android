package com.example.wanderwise.ui.profile.smallmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.wanderwise.R
import com.example.wanderwise.data.Result
import com.example.wanderwise.databinding.FragmentAboutUsBinding
import com.example.wanderwise.databinding.FragmentEmailChangeBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.profile.ProfileViewModel

class EmailChangeFragment : DialogFragment() {

    private var _binding: FragmentEmailChangeBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailChangeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.uploadButton.setOnClickListener {
            val email = binding.captionEdit.text.toString().trim()

            profileViewModel.updateEmail(email).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            isLoading(true)
                        }

                        is Result.Success -> {
                            showToast(result.data)
                            isLoading(false)
                        }

                        is Result.Error -> {
                            showToast(result.error)
                            isLoading(false)
                        }
                    }
                }
            }
        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun isLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

}