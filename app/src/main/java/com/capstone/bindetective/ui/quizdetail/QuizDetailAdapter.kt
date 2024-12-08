package com.capstone.bindetective.ui.quizdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bindetective.R
import com.capstone.bindetective.model.QuizDetailResponse

class QuizDetailAdapter(
    private val questions: List<QuizDetailResponse.Question>,
    private val onAnswerSelected: (Int, String) -> Unit
) : RecyclerView.Adapter<QuizDetailAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val radioGroupAnswers: RadioGroup = itemView.findViewById(R.id.radioGroupAnswers)

        fun bind(question: QuizDetailResponse.Question, position: Int) {
            tvQuestion.text = question.text
            radioGroupAnswers.removeAllViews()

            question.options.forEach { option ->
                val button = RadioButton(itemView.context).apply {
                    text = option.text
                    id = View.generateViewId()
                }
                radioGroupAnswers.addView(button)

                button.setOnClickListener {
                    onAnswerSelected(position, option.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder =
        QuizViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_quiz_detail, parent, false))

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) =
        holder.bind(questions[position], position)

    override fun getItemCount() = questions.size
}
