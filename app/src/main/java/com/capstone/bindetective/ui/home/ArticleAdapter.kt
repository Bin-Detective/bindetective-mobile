package com.capstone.bindetective.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bindetective.R
import com.capstone.bindetective.databinding.ItemArticleBinding
import com.capstone.bindetective.model.ArticleResponseItem
import com.squareup.picasso.Picasso

class ArticleAdapter(private var articles: List<ArticleResponseItem>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size

    // Method to update the adapter's data
    fun updateData(newArticles: List<ArticleResponseItem>) {
        articles = newArticles
        notifyDataSetChanged()  // Refresh the view
    }

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleResponseItem) {
            binding.textViewArticleTitle.text = article.title
            binding.textViewArticleDescription.text = article.description

            Log.d("ArticleAdapter", "Thumbnail URL: ${article.thumbnailUrl}")

            // Load thumbnail using Picasso
            if (!article.thumbnailUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(article.thumbnailUrl)
                    .placeholder(R.drawable.ic_profile_placeholder)  // Placeholder image
                    .error(R.drawable.ic_home)         // Error fallback image
                    .into(binding.imageViewArticleThumbnail)

                Log.d("Picasso", "Attempting to load from: ${article.thumbnailUrl}")
            } else {
                Log.e("Picasso", "Thumbnail URL is null or empty")
                binding.imageViewArticleThumbnail.setImageResource(R.drawable.ic_profile_placeholder)
            }
        }
    }
}
