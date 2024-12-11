package com.capstone.bindetective.ui.quiz

import QuizAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bindetective.R
import com.capstone.bindetective.model.QuizResponseItem
import com.capstone.bindetective.ui.quizdetail.QuizDetailFragment
import com.capstone.bindetective.ui.quiz.QuizViewModel

class QuizFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("QuizFragment", "onCreateView called")
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("QuizFragment", "onViewCreated called")

        recyclerView = view.findViewById(R.id.recyclerView)

        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Retrieve the score passed from QuizResultFragment
        val score = arguments?.getInt("score")

        quizViewModel.quizzes.observe(viewLifecycleOwner) { quizzes ->
            Log.d("QuizFragment", "Received quizzes: $quizzes")

            if (quizzes != null && quizzes.isNotEmpty()) {
                Log.d("QuizFragment", "Setting adapter with quiz items")

                // Pass quizzes and score to the adapter
                recyclerView.adapter = QuizAdapter(quizzes, score) { quiz ->
                    Log.d("QuizFragment", "Quiz item clicked: ${quiz.quizId}")

                    val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                    val quizDetailFragment = QuizDetailFragment()

                    val bundle = Bundle().apply {
                        putString("quizId", quiz.quizId)
                    }
                    quizDetailFragment.arguments = bundle

                    fragmentTransaction.replace(R.id.fragment_container, quizDetailFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()

                    Log.d("QuizFragment", "Navigated to QuizDetailFragment manually")
                }
            } else {
                Log.d("QuizFragment", "No quizzes available")
            }
        }

        quizViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Log.e("QuizFragment", "Error fetching quizzes: $errorMessage")
        }

        quizViewModel.fetchQuizzes()
        Log.d("QuizFragment", "Initiating quizViewModel.fetchQuizzes() call")
    }
}
