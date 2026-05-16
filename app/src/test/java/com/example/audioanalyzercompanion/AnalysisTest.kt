package com.example.audioanalyzercompanion

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AnalysisTest {

    @Test
    fun `analysis data class should be created correctly`() {
        // Arrange (set up test data)
        val analysis = Analysis(
            analysis_id = 9,
            user_filename = "user_track.wav",
            reference_filename = "reference_track.wav",
            status = "completed",
            overall = 32.3,
            production = 45.2,
            mix = 64.7,
            created_at = "2026-05-06T11:23:36.646266+00:00"
        )

        // Act (no action needed - just creation)

        // Assert (verify expectations)
        assertThat(analysis.analysis_id).isEqualTo(9)
        assertThat(analysis.user_filename).isEqualTo("user_track.wav")
        assertThat(analysis.status).isEqualTo("completed")
        assertThat(analysis.overall).isWithin(0.01).of(32.3)
    }

    @Test
    fun `scores data class should handle decimal values correctly`() {
        val scores = Scores(
            overall = 32.3,
            production = 45.2,
            mix = 64.7,
            loudness = 0.0,
            tonal = 62.7,
            dynamics = 0.0,
            stereo = 69.3
        )

        assertThat(scores.overall + scores.production + scores.mix)
            .isWithin(0.01).of(142.2)
    }
}