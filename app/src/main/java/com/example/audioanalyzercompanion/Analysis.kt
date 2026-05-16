package com.example.audioanalyzercompanion

// API Response for list of analyses
data class ApiResponse(
    val status: String,
    val message: String,
    val data: List<Analysis>
)

// API Response for single analysis
data class AnalysisDetailResponse(
    val status: String,
    val message: String,
    val data: AnalysisDetail
)

// Complete analysis detail
data class AnalysisDetail(
    val analysis_id: Int,
    val status: String,
    val user_filename: String,
    val reference_filename: String,
    val created_at: String,
    val scores: Scores,
    val insights: List<String>,
    val metrics: Metrics,
    val ai_advice: AiAdvice
)

// Scores
data class Scores(
    val overall: Double,
    val production: Double,
    val mix: Double,
    val loudness: Double,
    val tonal: Double,
    val dynamics: Double,
    val stereo: Double
)

// Metrics (detailed audio analysis)
data class Metrics(
    val user: TrackMetrics,
    val reference: TrackMetrics
)

data class TrackMetrics(
    val lufs: Double,
    val rms: Double,
    val peak: Double,
    val crest_factor: Double,
    val stereo_width: Double,
    val band_energies: BandEnergies
)

data class BandEnergies(
    val low: Double,
    val mid: Double,
    val high: Double
)

// AI Advice
data class AiAdvice(
    val summary: String,
    val bullets: List<String>
)

// For the list screen (simplified version)
data class Analysis(
    val analysis_id: Int,
    val user_filename: String,
    val reference_filename: String,
    val status: String,
    val overall: Double,
    val production: Double,
    val mix: Double,
    val created_at: String
)