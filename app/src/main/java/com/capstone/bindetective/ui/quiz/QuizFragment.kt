package com.capstone.bindetective.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bindetective.R
import com.capstone.bindetective.ui.quiz.QuizAdapter
import com.capstone.bindetective.ui.quiz.QuizViewModel

class QuizFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerView)

        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        quizViewModel.quizzes.observe(viewLifecycleOwner) { quizzes ->
            recyclerView.adapter = QuizAdapter(quizzes)
        }

        quizViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            println("Error fetching quizzes: $errorMessage")
        }

        quizViewModel.fetchQuizzes()
    }
}
