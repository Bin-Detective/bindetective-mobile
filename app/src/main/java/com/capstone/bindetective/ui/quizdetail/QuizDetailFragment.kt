package com.capstone.bindetective.ui.quizdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bindetective.R
import com.capstone.bindetective.model.SubmitQuizRequest
import com.capstone.bindetective.ui.quizresult.QuizResultFragment

class QuizDetailFragment : Fragment() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var recyclerViewQuestions: RecyclerView
    private lateinit var btnSubmit: Button

    private val selectedAnswers = mutableMapOf<Int, String>()
    private lateinit var quizDetailViewModel: QuizDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("QuizDetailFragment", "onCreateView called")
        return inflater.inflate(R.layout.fragment_quiz_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("QuizDetailFragment", "onViewCreated called")

        // Initialize UI elements
        tvTitle = view.findViewById(R.id.tvQuizTitle)
        tvDescription = view.findViewById(R.id.tvQuizDescription)
        recyclerViewQuestions = view.findViewById(R.id.recyclerViewQuiz)
        btnSubmit = view.findViewById(R.id.btnSubmit)

        // Setup ViewModel
        quizDetailViewModel = ViewModelProvider(this).get(QuizDetailViewModel::class.java)
        Log.d("QuizDetailFragment", "ViewModel initialized")

        // Get the Quiz ID from arguments
        val quizId = arguments?.getString("quizId")
        if (quizId == null) {
            Log.e("QuizDetailFragment", "No quizId found in arguments")
            return
        }
        Log.d("QuizDetailFragment", "Quiz ID: $quizId")

        // Fetch quiz details
        quizDetailViewModel.getQuizDetail(quizId)
        quizDetailViewModel.quizDetail.observe(viewLifecycleOwner) { quizDetail ->
            Log.d("QuizDetailFragment", "Quiz detail received")

            if (quizDetail != null) {
                Log.d("QuizDetailFragment", "Title: ${quizDetail.title}")
                Log.d("QuizDetailFragment", "Description: ${quizDetail.description}")

                tvTitle.text = quizDetail.title
                tvDescription.text = quizDetail.description

                recyclerViewQuestions.layoutManager = LinearLayoutManager(requireContext())
                recyclerViewQuestions.adapter = QuizDetailAdapter(
                    quizDetail.questions
                ) { position, selectedOptionId ->
                    Log.d("QuizDetailFragment", "Question $position selected answer $selectedOptionId")
                    selectedAnswers[position] = selectedOptionId
                }
            }
        }

        btnSubmit.setOnClickListener {
            Log.d("QuizDetailFragment", "Submit button clicked")

            if (selectedAnswers.size == recyclerViewQuestions.adapter?.itemCount) {
                Log.d("QuizDetailFragment", "All questions answered")
                submitAnswers(quizId, selectedAnswers)
            } else {
                Log.e("QuizDetailFragment", "Please answer all questions")
            }
        }
    }

    private fun submitAnswers(quizId: String, answers: Map<Int, String>) {
        Log.d("QuizDetailFragment", "Submitting quiz answers")

        val userId = "u7pzdJ3XGsNrtoQqx5ujUXqYOoJ3"

        val request = SubmitQuizRequest(
            userId = userId,
            answers = answers.map {
                SubmitQuizRequest.Answer(it.key.toString(), it.value)
            }
        )

        quizDetailViewModel.submitQuizAnswers(quizId, request) { response ->
            Log.d("QuizDetailFragment", "API response received")
            Log.d("QuizDetailFragment", "Response success: ${response.success}")
            Log.d("QuizDetailFragment", "Response message: ${response.message}")
            Log.d("QuizDetailFragment", "Score received: ${response.score}")

            val bundle = Bundle().apply {
                putBoolean("success", response.success)
                putString("message", response.message)
                response.score?.let { putInt("score", it) }
            }

            try {
                Log.d("QuizDetailFragment", "Navigating to QuizResultFragment")

                val resultFragment = QuizResultFragment().apply { arguments = bundle }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, resultFragment)
                    .commitAllowingStateLoss()
            } catch (e: Exception) {
                Log.e("QuizDetailFragment", "Navigation error: ${e.message}")
            }
        }
    }

}
