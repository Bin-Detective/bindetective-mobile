package com.capstone.bindetective.model

data class QuizDetailResponse(
    val title: String, // Title of the quiz
    val description: String, // Description of the quiz
    val questions: List<Question> // List of questions in the quiz
) {
    data class Question(
        val questionId: String, // Unique ID of the question
        val text: String, // The question text
        val type: String, // Type of question (e.g., multiple-choice)
        val options: List<Option> // List of options for the question
    )

    data class Option(
        val id: String, // Unique ID of the option
        val text: String, // Text of the option
        val isCorrect: Boolean // Whether this option is correct
    )
}
