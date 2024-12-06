import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bindetective.api.ApiConfig
import com.capstone.bindetective.model.ArticleResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

class HomeViewModel : ViewModel() {

    private val _articles = MutableLiveData<List<ArticleResponseItem>>()
    val articles: LiveData<List<ArticleResponseItem>> get() = _articles

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Fetch articles from API
    fun fetchArticles() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getAllArticles().execute()
                }

                if (response.isSuccessful) {
                    val articlesList = response.body()

                    if (articlesList != null && articlesList.isNotEmpty()) {
                        _articles.postValue(articlesList)
                    } else {
                        _error.postValue("No articles available.")
                    }
                } else {
                    _error.postValue("Failed to load articles. Error code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Network call error: ${e.message}")
                _error.postValue("Network error: ${e.message ?: "Unknown error"}")
            }
        }
    }
}
