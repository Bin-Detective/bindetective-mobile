package com.capstone.bindetective.ui.quizresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.bindetective.R
import com.capstone.bindetective.model.SubmitQuizResponse
import com.capstone.bindetective.ui.quizdetail.QuizDetailViewModel

class QuizResultFragment : Fragment() {

    private lateinit var tvResultMessage: TextView
    private lateinit var quizResultViewModel: QuizResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvResultMessage = view.findViewById(R.id.tvResultMessage)

        val success = arguments?.getBoolean("success", false) ?: false
        val message = arguments?.getString("message") ?: "No message"
        val score = arguments?.getInt("score", 0) ?: 0

        if (success) {
            Log.d("QuizResultFragment", "Quiz success with score: $score")
            tvResultMessage.text = "Quiz Submitted Successfully!\n$message\nYour Score: $score"
        } else {
            Log.d("QuizResultFragment", "Quiz submission failed")
            tvResultMessage.text = "Failed to Submit Quiz!\n$message"
        }
    }

}
