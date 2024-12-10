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
import com.capstone.bindetective.model.Answer
import com.capstone.bindetective.model.SubmitQuizRequest
import com.capstone.bindetective.model.SubmitQuizResponse
import com.capstone.bindetective.ui.quizresult.QuizResultViewModel
import com.capstone.bindetective.ui.quizresult.QuizResultFragment

class QuizDetailFragment : Fragment() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var recyclerViewQuestions: RecyclerView
    private lateinit var btnSubmit: Button

    private val selectedAnswers = mutableMapOf<Int, String>()
    private lateinit var quizDetailViewModel: QuizDetailViewModel
    private lateinit var quizResultViewModel: QuizResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle = view.findViewById(R.id.tvQuizTitle)
        tvDescription = view.findViewById(R.id.tvQuizDescription)
        recyclerViewQuestions = view.findViewById(R.id.recyclerViewQuiz)
        btnSubmit = view.findViewById(R.id.btnSubmit)

        quizDetailViewModel = ViewModelProvider(this)[QuizDetailViewModel::class.java]
        quizResultViewModel = ViewModelProvider(this)[QuizResultViewModel::class.java]

        val quizId = arguments?.getString("quizId")
        Log.d("QuizDetailFragment", "Quiz ID: $quizId")

        if (quizId == null) {
            Log.e("QuizDetailFragment", "No quizId found in arguments")
            return
        }

        quizDetailViewModel.getQuizDetail(quizId)

        quizDetailViewModel.quizDetail.observe(viewLifecycleOwner) { quizDetail ->
            if (quizDetail != null) {
                tvTitle.text = quizDetail.title
                tvDescription.text = quizDetail.description

                recyclerViewQuestions.layoutManager = LinearLayoutManager(requireContext())
                recyclerViewQuestions.adapter = QuizDetailAdapter(
                    quizDetail.questions
                ) { position, selectedOptionId ->
                    selectedAnswers[position] = selectedOptionId
                    Log.d("QuizDetailFragment", "Selected answer for position $position: $selectedOptionId")
                }
            }
        }

        btnSubmit.setOnClickListener {
            if (selectedAnswers.size == recyclerViewQuestions.adapter?.itemCount) {
                submitAnswers(quizId, selectedAnswers)
            } else {
                Log.e("QuizDetailFragment", "Please answer all questions")
            }
        }

        quizResultViewModel.submitQuizResult.observe(viewLifecycleOwner) { response ->
            navigateToResultFragment(response)
        }
    }

    private fun submitAnswers(quizId: String, answers: Map<Int, String>) {
        val userId = "u7pzdJ3XGsNrtoQqx5ujUXqYOoJ3"

        val formattedAnswers = quizDetailViewModel.quizDetail.value?.questions?.mapIndexed { index, question ->
            val selectedOptionId = answers[index]
            if (selectedOptionId != null) {
                Answer(
                    questionId = question.questionId,
                    selectedOptionId = selectedOptionId
                )
            } else {
                null
            }
        }?.filterNotNull() ?: emptyList()

        val request = SubmitQuizRequest(userId = userId, answers = formattedAnswers)

        Log.d("QuizDetailFragment", "Request: $request")
        quizResultViewModel.submitQuizAnswers(quizId, request)
    }

    private fun navigateToResultFragment(response: SubmitQuizResponse?) {
        val bundle = Bundle().apply {
            putString("message", response?.message ?: "No message")
            response?.score?.let { putInt("score", it) }
        }

        val resultFragment = QuizResultFragment().apply { arguments = bundle }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, resultFragment)
            .addToBackStack(null)   // Ensures proper back navigation stack
            .commitAllowingStateLoss()

        Log.d("QuizDetailFragment", "Navigating to QuizResultFragment")
    }

}
