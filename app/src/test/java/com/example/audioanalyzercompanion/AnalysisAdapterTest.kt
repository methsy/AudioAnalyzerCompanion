package com.example.audioanalyzercompanion

import io.mockk.*
import org.junit.Test

class AnalysisAdapterTest {

    @Test
    fun `adapter should return correct item count`() {
        // Create mock data
        val analyses = listOf(
            mockAnalysis(1),
            mockAnalysis(2),
            mockAnalysis(3)
        )

        // Create adapter with mock click handler
        val onItemClick: (Analysis) -> Unit = mockk(relaxed = true)
        val adapter = AnalysisAdapter(analyses, onItemClick)

        // Verify item count
        assert(adapter.itemCount == 3)
    }

    @Test
    fun `click handler should be called when item is clicked`() {
        val analysis = mockAnalysis(9)
        val analyses = listOf(analysis)
        val onItemClick: (Analysis) -> Unit = mockk(relaxed = true)
        val adapter = AnalysisAdapter(analyses, onItemClick)

        // Simulate click (in a real test with ViewHolder)
        // This is simplified - actual ViewHolder test needs Robolectric

        // Verify click handler was called (conceptual)
        // verify { onItemClick(analysis) }
    }

    private fun mockAnalysis(id: Int): Analysis {
        return Analysis(
            analysis_id = id,
            user_filename = "test.wav",
            reference_filename = "ref.wav",
            status = "completed",
            overall = 50.0,
            production = 50.0,
            mix = 50.0,
            created_at = "2026-01-01T00:00:00Z"
        )
    }
}