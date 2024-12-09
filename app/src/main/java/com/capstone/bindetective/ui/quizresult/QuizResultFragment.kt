package com.capstone.bindetective.ui.quizresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.capstone.bindetective.R
import com.capstone.bindetective.model.SubmitQuizRequest
import com.capstone.bindetective.model.SubmitQuizResponse

class QuizResultFragment : Fragment() {

    private lateinit var tvQuizResultTitle: TextView
    private lateinit var tvResultMessage: TextView
    private val quizResultViewModel: QuizResultViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment_quiz_result layout
        return inflater.inflate(R.layout.fragment_quiz_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the TextView for displaying the result message
        tvResultMessage = view.findViewById(R.id.tvResultMessage)

        // Observe the LiveData for quiz results
        quizResultViewModel.submitQuizResult.observe(viewLifecycleOwner) { response ->
            updateUI(response)
        }
    }

    private fun updateUI(response: SubmitQuizResponse?) {
        if (response != null) {
            if (response.score != null) {
                // Quiz submission was successful
                Log.d("QuizResultFragment", "Quiz success with score: ${response.score}")

                tvResultMessage.text = "Your Score: ${response.score}\n${response.message}"
                tvQuizResultTitle.text = "Quiz Submitted Successfully!"
            } else {
                Log.d("QuizResultFragment", "Quiz submission failed")
                tvQuizResultTitle.text = "Quiz Submission Failed!"
                tvResultMessage.text = "Message: ${response?.message ?: "Unknown error"}"
            }
        } else {
            Log.e("QuizResultFragment", "Response is null")
            tvQuizResultTitle.text = "Quiz Submission Failed!"
            tvResultMessage.text = "An error occurred. Please try again."
        }
    }
}
