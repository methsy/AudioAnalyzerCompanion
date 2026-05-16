package com.example.audioanalyzercompanion

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        // Start a local mock server
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Create a Retrofit instance pointing to the mock server
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getAnalyses should return list of analyses on successful response`() = runBlocking {
        // Arrange: Mock the server response
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "status": "success",
                    "message": "Analyses retrieved successfully",
                    "data": [
                        {
                            "analysis_id": 9,
                            "user_filename": "user_track.wav",
                            "reference_filename": "reference_track.wav",
                            "status": "completed",
                            "overall": 32.3,
                            "production": 45.2,
                            "mix": 64.7,
                            "created_at": "2026-05-06T11:23:36.646266+00:00"
                        },
                        {
                            "analysis_id": 8,
                            "user_filename": "user_track.wav",
                            "reference_filename": "reference_track.wav",
                            "status": "completed",
                            "overall": 32.3,
                            "production": 45.2,
                            "mix": 64.7,
                            "created_at": "2026-05-04T12:05:26.978706+00:00"
                        }
                    ]
                }
            """.trimIndent())

        mockWebServer.enqueue(mockResponse)

        // Act: Call the API
        val response = apiService.getAnalyses().execute()

        // Assert: Verify the response
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.body()?.status).isEqualTo("success")
        assertThat(response.body()?.data).hasSize(2)
        assertThat(response.body()?.data?.get(0)?.analysis_id).isEqualTo(9)
        assertThat(response.body()?.data?.get(1)?.analysis_id).isEqualTo(8)
    }

    @Test
    fun `getAnalyses should handle empty list response`() = runBlocking {
        // Arrange
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "status": "success",
                    "message": "No analyses found",
                    "data": []
                }
            """.trimIndent())

        mockWebServer.enqueue(mockResponse)

        // Act
        val response = apiService.getAnalyses().execute()

        // Assert
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.body()?.data).isEmpty()
    }

    @Test
    fun `getAnalysisById should return single analysis on success`() = runBlocking {
        // Arrange
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "status": "success",
                    "message": "Analysis retrieved successfully",
                    "data": {
                        "analysis_id": 9,
                        "status": "completed",
                        "user_filename": "user_track.wav",
                        "reference_filename": "reference_track.wav",
                        "created_at": "2026-05-06T11:23:36.646266+00:00",
                        "scores": {
                            "overall": 32.3,
                            "production": 45.2,
                            "mix": 64.7,
                            "loudness": 0.0,
                            "tonal": 62.7,
                            "dynamics": 0.0,
                            "stereo": 69.3
                        },
                        "insights": [
                            "Your track is quieter than the reference.",
                            "Your track has less high-end brightness."
                        ],
                        "metrics": {
                            "user": {
                                "lufs": -30.65,
                                "rms": 0.0343,
                                "peak": 0.2983,
                                "crest_factor": 8.694,
                                "stereo_width": 0.334,
                                "band_energies": {
                                    "low": 0.2956,
                                    "mid": 0.4906,
                                    "high": 0.2025
                                }
                            },
                            "reference": {
                                "lufs": -10.58,
                                "rms": 0.3245,
                                "peak": 1.0,
                                "crest_factor": 3.082,
                                "stereo_width": 0.181,
                                "band_energies": {
                                    "low": 0.2060,
                                    "mid": 0.5253,
                                    "high": 0.2649
                                }
                            }
                        },
                        "ai_advice": {
                            "summary": "Your track needs significant adjustments in loudness and dynamics.",
                            "bullets": [
                                "Increase overall loudness to aim for a LUFS closer to -10 dB.",
                                "Apply compression to control peaks."
                            ]
                        }
                    }
                }
            """.trimIndent())

        mockWebServer.enqueue(mockResponse)

        // Act
        val response = apiService.getAnalysisById(9).execute()

        // Assert
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.body()?.data?.analysis_id).isEqualTo(9)
        assertThat(response.body()?.data?.status).isEqualTo("completed")
        assertThat(response.body()?.data?.scores?.overall).isWithin(0.01).of(32.3)
        assertThat(response.body()?.data?.insights).hasSize(2)
        assertThat(response.body()?.data?.metrics?.user?.lufs).isWithin(0.01).of(-30.65)
        assertThat(response.body()?.data?.ai_advice?.bullets).hasSize(2)
    }

    @Test
    fun `getAnalysisById should handle 404 not found`() = runBlocking {
        // Arrange
        val mockResponse = MockResponse()
            .setResponseCode(404)
            .setBody("{\"status\": \"error\", \"message\": \"Analysis not found\"}")

        mockWebServer.enqueue(mockResponse)

        // Act
        val response = apiService.getAnalysisById(999).execute()

        // Assert
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(404)
    }

    @Test(expected = Exception::class)
    fun `getAnalysisById should handle malformed JSON gracefully`() = runBlocking {
        // Arrange: Return invalid JSON
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("This is not valid JSON")

        mockWebServer.enqueue(mockResponse)

        // Act
        val response = apiService.getAnalysisById(9).execute()

        // Assert: Should fail because JSON can't be parsed
        assertThat(response.isSuccessful).isFalse()
    }

    @Test
    fun `getAnalyses should handle server error 500`() = runBlocking {
        // Arrange
        val mockResponse = MockResponse()
            .setResponseCode(500)

        mockWebServer.enqueue(mockResponse)

        // Act
        val response = apiService.getAnalyses().execute()

        // Assert
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(500)
    }
}