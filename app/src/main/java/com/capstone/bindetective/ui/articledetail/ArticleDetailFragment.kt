package com.capstone.bindetective.ui.articledetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.bindetective.R
import com.capstone.bindetective.databinding.FragmentArticleDetailBinding
import com.capstone.bindetective.model.ArticleResponseItem
import com.squareup.picasso.Picasso

class ArticleDetailFragment : Fragment() {

    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleDetailBinding.inflate(inflater, container, false)

        // Back button listener
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Retrieve article data from arguments
        val article: ArticleResponseItem? = arguments?.getParcelable("article")
        article?.let { displayArticleDetails(it) }

        return binding.root
    }


    private fun displayArticleDetails(article: ArticleResponseItem) {
        binding.textViewTitle.text = article.title
        binding.textViewDescription.text = article.description
        binding.textViewContent.text = article.content // Display the article content

        // Load thumbnail using Picasso
        if (!article.thumbnailUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(article.thumbnailUrl)
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_home)
                .into(binding.imageViewThumbnail)
        } else {
            binding.imageViewThumbnail.setImageResource(R.drawable.ic_profile_placeholder)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
