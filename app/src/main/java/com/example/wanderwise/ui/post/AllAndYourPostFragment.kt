package com.example.wanderwise.ui.post

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wanderwise.R
import com.example.wanderwise.data.response.CreatedAt
import com.example.wanderwise.data.response.GetAllPostResponse
import com.example.wanderwise.data.response.PostsItem
import com.example.wanderwise.databinding.FragmentAllAndYourPostBinding
import com.example.wanderwise.databinding.FragmentPostBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.adapter.CityExploreAdapter
import com.example.wanderwise.ui.adapter.PostAdapter
//import com.example.wanderwise.ui.adapter.PostAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class AllAndYourPostFragment : Fragment() {

    private var _binding: FragmentAllAndYourPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterUserPost: PostAdapter

    private val postViewModel by viewModels<GetPostViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var position: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllAndYourPostBinding.inflate(inflater, container, false)
        val view = binding.root

        postViewModel.getSessionUser().observe(viewLifecycleOwner) { uid ->
            val uidUser = uid.uid
            val db = Firebase.firestore

            arguments?.let {
                position = it.getInt(ARG_POSITION)
            }

            if (position == 1) {
                val userAllPosts = ArrayList<PostsItem>()
                showLoading(true)

                db.collection("posts")
                    .get()
                    .addOnSuccessListener {
                        it.documents.forEach() { doc ->

                            userAllPosts.add(
                                PostsItem(
                                    image = doc.getString("image"),
                                    createdAt = CreatedAt(
                                        seconds = doc.getTimestamp("createdAt")!!.seconds,
                                        nanoseconds = doc.getTimestamp("createdAt")!!.nanoseconds
                                    ),
                                    caption = doc.getString("caption"),
                                    id = doc.getString("userId"),
                                    title = doc.getString("city"),
                                    idPost =  doc.getString("idPost"),
                                    name = doc.getString("name")
                                )
                            )
                        }

                        showLoading(false)
                        adapterUserPost = PostAdapter(requireContext(), userAllPosts)
                        binding.rvAllYourPost.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvAllYourPost.setHasFixedSize(true)
                        binding.rvAllYourPost.adapter = adapterUserPost
                    }
            } else {
                val userPosts = ArrayList<PostsItem>()
                showLoading(true)

                db.collection("posts").whereEqualTo("userId", uidUser)
                    .get()
                    .addOnSuccessListener {
                        it.documents.forEach() {doc ->

                            userPosts.add(
                                PostsItem(
                                    image = doc.getString("image"),
                                    createdAt = CreatedAt(
                                        seconds = doc.getTimestamp("createdAt")!!.seconds,
                                        nanoseconds = doc.getTimestamp("createdAt")!!.nanoseconds
                                    ),
                                    caption = doc.getString("caption"),
                                    id = doc.getString("userId"),
                                    title = doc.getString("city"),
                                    idPost =  doc.getString("idPost"),
                                    name = doc.getString("name")
                                )
                            )
                        }

                        showLoading(false)
                        adapterUserPost = PostAdapter(requireContext(), userPosts)
                        binding.rvAllYourPost.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvAllYourPost.setHasFixedSize(true)
                        binding.rvAllYourPost.adapter = adapterUserPost
                    }
            }
        }


        return view
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
    }
}