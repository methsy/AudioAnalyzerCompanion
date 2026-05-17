package com.example.audioanalyzercompanion

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The main entry point of the application that displays a list of audio analysis results.
 *
 * This activity initializes the UI, sets up a [RecyclerView] to show analysis items,
 * and fetches the data from a remote API using Coroutines and Retrofit.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnalysisAdapter
    private val analysesList = mutableListOf<Analysis>()

    /**
     * Initializes the activity, sets the content view, and triggers UI setup and data fetching.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     * this Bundle contains the data it most recently supplied in [onSaveInstanceState].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        fetchAnalyses()
    }

    /**
     * Configures the [RecyclerView] with a [LinearLayoutManager] and an [AnalysisAdapter].
     *
     * It also defines the click behavior for items in the list, showing a short Toast message
     * with the filename of the selected analysis.
     */
    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AnalysisAdapter(analysesList) { analysis ->
            Toast.makeText(this, "Clicked: ${analysis.user_filename}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
    }

    /**
     * Fetches the list of analyses from the remote API asynchronously.
     *
     * This method uses [CoroutineScope] with [Dispatchers.IO] to perform the network request.
     * Upon success, it updates the local [analysesList] and notifies the [adapter] on the main thread.
     * In case of failure or network error, a [Toast] message is displayed to the user.
     */
    private fun fetchAnalyses() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getAnalyses().execute()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { apiResponse ->
                            analysesList.clear()
                            analysesList.addAll(apiResponse.data)
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}