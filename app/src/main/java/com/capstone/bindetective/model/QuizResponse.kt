package com.capstone.bindetective.model

import java.io.Serializable

data class QuizResponseItem(
    val quizId: String,
    val title: String,
    val description: String
) : Serializable
