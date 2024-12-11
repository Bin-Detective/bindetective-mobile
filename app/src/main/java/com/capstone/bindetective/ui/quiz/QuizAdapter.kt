import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bindetective.R
import com.capstone.bindetective.model.QuizResponseItem

class QuizAdapter(
    private val quizzes: List<QuizResponseItem>,
    private val score: Int?,  // This is the overall quiz score
    private val onQuizClick: (QuizResponseItem) -> Unit
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.quizTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.quizDesc)
        val scoreTextView: TextView = itemView.findViewById(R.id.quizScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quizItem = quizzes[position]

        // Bind quiz title and description
        holder.titleTextView.text = quizItem.title
        holder.descriptionTextView.text = quizItem.description

        // **Show the overall quiz score once per row item**
        if (position == 0 && score != null) {
            holder.scoreTextView.text = "Your Score: $score"
            holder.scoreTextView.visibility = View.VISIBLE
        } else {
            holder.scoreTextView.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onQuizClick(quizItem)
        }
    }

    override fun getItemCount(): Int = quizzes.size
}
