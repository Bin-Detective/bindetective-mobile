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
import com.capstone.bindetective.ui.articledetail.ArticleDetailFragment
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

        // Initialize RecyclerView and Adapter
        articleAdapter = ArticleAdapter(listOf()) { article ->
            navigateToArticleDetail(article)
        }
        binding.recyclerViewArticles.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewArticles.adapter = articleAdapter

        setUserInfo()
        observeViewModel()
        homeViewModel.fetchArticles()

        return binding.root
    }

    private fun observeViewModel() {
        homeViewModel.articles.observe(viewLifecycleOwner) { articles ->
            if (articles != null) {
                articleAdapter.updateData(articles)
            }
        }

        homeViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToArticleDetail(article: ArticleResponseItem) {
        val bundle = Bundle().apply {
            putParcelable("article", article)
        }
        val fragment = ArticleDetailFragment().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
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
