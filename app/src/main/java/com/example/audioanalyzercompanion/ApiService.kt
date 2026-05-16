package com.example.audioanalyzercompanion

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("analyses")
    fun getAnalyses(): Call<ApiResponse>

    @GET("analyses/{id}")
    fun getAnalysisById(@Path("id") id: Int): Call<AnalysisDetailResponse>
}