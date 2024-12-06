package com.capstone.bindetective.model

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("ArticleResponse")
	val articleResponse: List<ArticleResponseItem>
)

data class ArticleResponseItem(

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("contentId")
	val contentId: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("dateAdded")
	val dateAdded: String,

	@field:SerializedName("content")
	val content: String,

	@field:SerializedName("thumbnailUrl")
	val thumbnailUrl: String
)
