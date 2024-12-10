package com.capstone.bindetective.ui.quizresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.capstone.bindetective.R

class QuizResultFragment : Fragment() {

    private lateinit var tvQuizResultTitle: TextView
    private lateinit var tvResultMessage: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvQuizResultTitle = view.findViewById(R.id.tvQuizResultTitle)
        tvResultMessage = view.findViewById(R.id.tvResultMessage)

        // Retrieve the bundle arguments passed during fragment creation
        val message = arguments?.getString("message") ?: "No message"
        val score = arguments?.getInt("score")

        tvResultMessage.text = message
        tvQuizResultTitle.text = if (score != null) "Score: $score" else "No Score"

        Log.d("QuizResultFragment", "Quiz Result Title: ${tvQuizResultTitle.text}")
        Log.d("QuizResultFragment", "Quiz Result Message: ${tvResultMessage.text}")
    }
}
