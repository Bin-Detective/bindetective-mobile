package com.capstone.bindetective.ui.quizresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.capstone.bindetective.R
import com.capstone.bindetective.databinding.FragmentQuizResultBinding
import com.capstone.bindetective.ui.quiz.QuizFragment

class QuizResultFragment : Fragment() {

    private var _binding: FragmentQuizResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button listener to navigate to QuizFragment
        binding.btnBackToQuiz.setOnClickListener {
            val score = arguments?.getInt("score")

            // Create a new instance of QuizFragment and pass the score back in a Bundle
            val quizFragment = QuizFragment().apply {
                arguments = Bundle().apply {
                    putInt("score", score ?: 0)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, quizFragment)  // Replace with QuizFragment
                .addToBackStack(null)  // Keep it in the back stack
                .commit()

            Log.d("QuizResultFragment", "Navigating back to QuizFragment with score: $score")
        }

        // Retrieve the bundle arguments passed during fragment creation
        val message = arguments?.getString("message") ?: "No message"
        val score = arguments?.getInt("score")

        binding.tvResultMessage.text = message
        binding.tvQuizResultTitle.text = if (score != null) "Score: $score" else "No Score"

        Log.d("QuizResultFragment", "Quiz Result Title: ${binding.tvQuizResultTitle.text}")
        Log.d("QuizResultFragment", "Quiz Result Message: ${binding.tvResultMessage.text}")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
