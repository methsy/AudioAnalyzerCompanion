package com.example.audioanalyzercompanion

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var analysisTitle: TextView
    private lateinit var statusValue: TextView
    private lateinit var overallScore: TextView
    private lateinit var productionScore: TextView
    private lateinit var mixScore: TextView
    private lateinit var loudnessScore: TextView
    private lateinit var tonalScore: TextView
    private lateinit var dynamicsScore: TextView
    private lateinit var stereoScore: TextView
    private lateinit var userTrackDetail: TextView
    private lateinit var referenceTrackDetail: TextView
    private lateinit var createdDate: TextView
    private lateinit var insightsList: TextView
    private lateinit var aiSummary: TextView
    private lateinit var aiBullets: TextView
    private lateinit var metricsChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.analysis_details_title)

        // Initialize views
        analysisTitle = findViewById(R.id.analysisTitle)
        statusValue = findViewById(R.id.statusValue)
        overallScore = findViewById(R.id.overallScore)
        productionScore = findViewById(R.id.productionScore)
        mixScore = findViewById(R.id.mixScore)
        loudnessScore = findViewById(R.id.loudnessScore)
        tonalScore = findViewById(R.id.tonalScore)
        dynamicsScore = findViewById(R.id.dynamicsScore)
        stereoScore = findViewById(R.id.stereoScore)
        userTrackDetail = findViewById(R.id.userTrackDetail)
        referenceTrackDetail = findViewById(R.id.referenceTrackDetail)
        createdDate = findViewById(R.id.createdDate)
        insightsList = findViewById(R.id.insightsList)
        aiSummary = findViewById(R.id.aiSummary)
        aiBullets = findViewById(R.id.aiBullets)
        metricsChart = findViewById(R.id.metricsChart)

        // Setup chart
        MetricsChartHelper.setupChart(metricsChart)

        // Get analysis ID from intent
        val analysisId = intent.getIntExtra("ANALYSIS_ID", -1)

        if (analysisId != -1) {
            fetchAnalysisDetails(analysisId)
        } else {
            statusValue.text = getString(R.string.error)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun fetchAnalysisDetails(analysisId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getAnalysisById(analysisId).execute()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { apiResponse ->
                            displayAnalysisDetails(apiResponse.data)
                        }
                    } else {
                        statusValue.text = "${getString(R.string.error)}: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    statusValue.text = "${getString(R.string.error)}: ${e.message}"
                }
            }
        }
    }

    private fun displayAnalysisDetails(analysis: AnalysisDetail) {
        // Basic info
        analysisTitle.text = getString(R.string.analysis_number, analysis.analysis_id)
        statusValue.text = analysis.status
        userTrackDetail.text = getString(R.string.user_track_label, analysis.user_filename ?: getString(R.string.unknown))
        referenceTrackDetail.text = getString(R.string.reference_track_label, analysis.reference_filename ?: getString(R.string.unknown))

        // Format date safely
        val dateStr = analysis.created_at?.take(10) ?: getString(R.string.unknown)
        createdDate.text = getString(R.string.created_label, dateStr)

        // Scores
        overallScore.text = getString(R.string.score_label, analysis.scores.overall)
        productionScore.text = getString(R.string.score_label, analysis.scores.production)
        mixScore.text = getString(R.string.score_label, analysis.scores.mix)
        loudnessScore.text = getString(R.string.score_label, analysis.scores.loudness)
        tonalScore.text = getString(R.string.score_label, analysis.scores.tonal)
        dynamicsScore.text = getString(R.string.score_label, analysis.scores.dynamics)
        stereoScore.text = getString(R.string.score_label, analysis.scores.stereo)

        // Insights (bullet points)
        val insightsText = analysis.insights.joinToString("\n• ") { "• $it" }
        insightsList.text = insightsText

        // AI Advice
        aiSummary.text = analysis.ai_advice.summary
        val bulletsText = analysis.ai_advice.bullets.joinToString("\n• ") { "• $it" }
        aiBullets.text = bulletsText

        // Setup chart
        setupMetricsChart(analysis)
    }

    private fun setupMetricsChart(analysis: AnalysisDetail) {
        val userMetrics = analysis.metrics.user
        val referenceMetrics = analysis.metrics.reference

        // Add more metrics for better comparison
        val labels = listOf("LUFS", "RMS", "Stereo Width", "Crest Factor")

        val userValues = listOf(
            normalizeLufs(userMetrics.lufs),
            userMetrics.rms.toFloat(),
            userMetrics.stereo_width.toFloat(),
            userMetrics.crest_factor.toFloat().coerceAtMost(15f)
        )

        val referenceValues = listOf(
            normalizeLufs(referenceMetrics.lufs),
            referenceMetrics.rms.toFloat(),
            referenceMetrics.stereo_width.toFloat(),
            referenceMetrics.crest_factor.toFloat().coerceAtMost(15f)
        )

        MetricsChartHelper.setChartData(metricsChart, userValues, referenceValues, labels)
    }

    private fun normalizeLufs(lufs: Double): Float {
        // LUFS is typically negative (e.g., -30 to -10)
        // Convert to positive scale for better visualization
        val normalized = (lufs + 40).toFloat()
        return normalized.coerceIn(0f, 50f)
    }
}