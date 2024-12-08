import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bindetective.R
import com.capstone.bindetective.model.QuizResponseItem

class QuizAdapter(
    private val quizzes: List<QuizResponseItem>,
    private val onQuizClick: (QuizResponseItem) -> Unit
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.quizTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.quizDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.titleTextView.text = quiz.title
        holder.descriptionTextView.text = quiz.description

        holder.itemView.setOnClickListener {
            onQuizClick(quiz)
        }
    }

    override fun getItemCount() = quizzes.size
}
