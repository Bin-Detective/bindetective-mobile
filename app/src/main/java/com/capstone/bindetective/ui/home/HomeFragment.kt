package com.capstone.bindetective.ui.home

import HomeViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.bindetective.R
import com.capstone.bindetective.databinding.FragmentHomeBinding
import com.capstone.bindetective.model.ArticleResponseItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Set up RecyclerView for articles
        articleAdapter = ArticleAdapter(listOf())
        binding.recyclerViewArticles.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewArticles.adapter = articleAdapter

        // Set user info
        setUserInfo()

        // Observe LiveData for articles
        observeViewModel()

        // Fetch articles
        homeViewModel.fetchArticles()

        return binding.root
    }

    private fun observeViewModel() {
        homeViewModel.articles.observe(viewLifecycleOwner, Observer { articles ->
            if (articles != null) {
                Log.d("HomeFragment", "Articles received: ${articles.size}")
                articleAdapter.updateData(articles)
            }
        })

        homeViewModel.error.observe(viewLifecycleOwner, Observer { errorMsg ->
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
            Log.e("HomeFragment", "API Error: $errorMsg")
        })
    }

    private fun setUserInfo() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val googleAccount = GoogleSignIn.getLastSignedInAccount(requireContext())

        val displayName = firebaseUser?.displayName ?: googleAccount?.displayName ?: "Unknown User"
        val profilePictureUrl = firebaseUser?.photoUrl?.toString() ?: googleAccount?.photoUrl?.toString()

        binding.textViewName.text = displayName

        if (profilePictureUrl != null) {
            Picasso.get()
                .load(profilePictureUrl)
                .placeholder(R.drawable.ic_profile_placeholder)
                .into(binding.imageViewProfilePicture)
        } else {
            binding.imageViewProfilePicture.setImageResource(R.drawable.ic_profile_placeholder)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
