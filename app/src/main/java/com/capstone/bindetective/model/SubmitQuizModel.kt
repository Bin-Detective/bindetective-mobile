package com.capstone.bindetective.model

// Data class representing a single quiz answer
data class Answer(
    val questionId: String,
    val selectedOptionId: String
)

// Data class representing a quiz answer request
data class SubmitQuizRequest(
    val userId: String,
    val answers: List<Answer>
)

// Data class representing the response after submitting the quiz
data class SubmitQuizResponse(
    val message: String?,
    val score: Int?
)
