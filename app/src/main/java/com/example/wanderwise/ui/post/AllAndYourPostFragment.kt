package com.example.wanderwise.ui.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wanderwise.R
import com.example.wanderwise.data.response.GetAllPostResponse
import com.example.wanderwise.databinding.FragmentAllAndYourPostBinding
import com.example.wanderwise.databinding.FragmentPostBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.adapter.PostAdapter
import com.google.android.material.snackbar.Snackbar

class AllAndYourPostFragment : Fragment() {

    private var _binding: FragmentAllAndYourPostBinding? = null
    private val binding get() = _binding!!

    private val postViewModel by viewModels<GetPostViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var position: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllAndYourPostBinding.inflate(inflater, container, false)
        val view = binding.root

        arguments?.let {
            position = it.getInt(ARG_POSITION)
        }

        if (position == 1) {
            postViewModel.allPost.observe(viewLifecycleOwner) { allPost ->
                setAllPostData(allPost)
            }
        } else {
            postViewModel.allUser.observe(viewLifecycleOwner) { userPost ->
                setAllUserData(userPost)
            }
        }

        postViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        postViewModel.snackbar.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snack ->
                Snackbar.make(
                    view,
                    snack,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        postViewModel.getAllPosts()
        postViewModel.getUserPost()

        return view
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setAllPostData(followers: ArrayList<GetAllPostResponse>){
        val adapter = PostAdapter(requireActivity())
        adapter.submitList(followers)
        binding.rvAllYourPost.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAllYourPost.setHasFixedSize(true)
        binding.rvAllYourPost.adapter = adapter
    }

    private fun setAllUserData(following: ArrayList<GetAllPostResponse>){
        val adapter = PostAdapter(requireActivity())
        adapter.submitList(following)
        binding.rvAllYourPost.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAllYourPost.setHasFixedSize(true)
        binding.rvAllYourPost.adapter = adapter
    }

    companion object {
        const val ARG_POSITION = "position"
    }
}