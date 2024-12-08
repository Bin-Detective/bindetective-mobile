package com.capstone.bindetective.ui.quizdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bindetective.R
import com.capstone.bindetective.model.QuizDetailResponse
import com.capstone.bindetective.ui.quizdetail.QuizDetailViewModel

class QuizDetailFragment : Fragment() {

    private lateinit var quizDetailViewModel: QuizDetailViewModel
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var recyclerViewQuestions: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("QuizDetailFragment", "onCreateView called")
        return inflater.inflate(R.layout.fragment_quiz_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("QuizDetailFragment", "onViewCreated called")

        tvTitle = view.findViewById(R.id.tvQuizTitle)
        tvDescription = view.findViewById(R.id.tvQuizDescription)
        recyclerViewQuestions = view.findViewById(R.id.recyclerViewQuiz)

        quizDetailViewModel = ViewModelProvider(this).get(QuizDetailViewModel::class.java)

        val quizId = arguments?.getString("quizId") ?: run {
            Log.e("QuizDetailFragment", "quizId is null")
            return
        }
        Log.d("QuizDetailFragment", "Received quizId: $quizId")

        quizDetailViewModel.getQuizDetail(quizId)

        quizDetailViewModel.quizDetail.observe(viewLifecycleOwner) { quizDetail ->
            Log.d("QuizDetailFragment", "QuizDetail received: $quizDetail")

            tvTitle.text = quizDetail.title
            tvDescription.text = quizDetail.description

            recyclerViewQuestions.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewQuestions.adapter = QuizDetailAdapter(
                quizDetail.questions
            ) { _, selectedOptionId ->
                Log.d("QuizDetailFragment", "Option selected with ID: $selectedOptionId")
            }
        }

        quizDetailViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            Log.e("QuizDetailFragment", "API Error: $errorMsg")
        }
    }
}
