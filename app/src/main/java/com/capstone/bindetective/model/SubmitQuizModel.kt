package com.capstone.bindetective.model

// Data class representing a quiz answer request
data class SubmitQuizRequest(
    val userId: String,
    val answers: List<Answer>
) {
    data class Answer(
        val questionId: String,
        val selectedOptionId: String
    )
}

// Data class representing the response after submitting the quiz
data class SubmitQuizResponse(
    val success: Boolean,   // Indicates if the quiz submission was successful
    val message: String?,   // Confirmation or error message
    val score: Int?         // User's score after quiz submission
)
