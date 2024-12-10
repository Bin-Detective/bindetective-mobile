package com.capstone.bindetective.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bindetective.R
import com.capstone.bindetective.databinding.ItemArticleBinding
import com.capstone.bindetective.model.ArticleResponseItem
import com.squareup.picasso.Picasso

class ArticleAdapter(
    private var articles: List<ArticleResponseItem>,
    private val onArticleClick: (ArticleResponseItem) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size

    fun updateData(newArticles: List<ArticleResponseItem>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleResponseItem) {
            binding.textViewArticleTitle.text = article.title
            binding.textViewArticleDescription.text = article.description

            // Load thumbnail using Picasso
            if (!article.thumbnailUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(article.thumbnailUrl)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_home)
                    .into(binding.imageViewArticleThumbnail)
            } else {
                binding.imageViewArticleThumbnail.setImageResource(R.drawable.ic_profile_placeholder)
            }

            // Set click listener for the item
            binding.root.setOnClickListener {
                onArticleClick(article)
            }
        }
    }
}
