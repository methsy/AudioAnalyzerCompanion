package com.example.audioanalyzercompanion

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit interface defining the API endpoints for audio analysis data.
 */
interface ApiService {
    /**
     * Retrieves a list of all audio analyses.
     *
     * @return A [Call] object that, when executed, returns an [ApiResponse] containing the list of analyses.
     */
    @GET("analyses")
    fun getAnalyses(): Call<ApiResponse>

    /**
     * Retrieves the detailed information for a specific audio analysis by its ID.
     *
     * @param id The unique identifier of the analysis.
     * @return A [Call] object that, when executed, returns an [AnalysisDetailResponse] with specific metrics.
     */
    @GET("analyses/{id}")
    fun getAnalysisById(@Path("id") id: Int): Call<AnalysisDetailResponse>
}