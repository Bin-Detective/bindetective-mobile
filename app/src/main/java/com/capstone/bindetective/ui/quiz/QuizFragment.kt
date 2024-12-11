package com.capstone.bindetective.ui.quiz

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

class QuizFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerView)
        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe the quizzes data
        quizViewModel.quizzes.observe(viewLifecycleOwner) { quizzes ->
            val savedScore = quizViewModel.getScore()  // Retrieve the saved score
            recyclerView.adapter = QuizAdapter(quizzes, savedScore) { quizItem ->
                Log.d("QuizFragment", "Quiz item clicked: ${quizItem.quizId}")

                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                val quizDetailFragment = QuizDetailFragment()

                val bundle = Bundle().apply {
                    putString("quizId", quizItem.quizId)
                }
                quizDetailFragment.arguments = bundle

                fragmentTransaction.replace(R.id.fragment_container, quizDetailFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }

        quizViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            Log.e("QuizFragment", "API Error: $errorMsg")
        }

        quizViewModel.score.observe(viewLifecycleOwner) { score ->
            if (score != null) {
                Log.d("QuizFragment", "User's score: $score")
            }
        }

        quizViewModel.fetchQuizzes()  // Fetch quiz data
    }
}
