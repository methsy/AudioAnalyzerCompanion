package com.example.audioanalyzercompanion

import android.graphics.Color

object ScoreColorHelper {

    fun getColorForScore(score: Double): Int {
        return when (score.toInt()) {
            in 80..100 -> Color.parseColor("#4CAF50")  // Green
            in 60..79 -> Color.parseColor("#2196F3")   // Blue
            in 40..59 -> Color.parseColor("#FFC107")   // Amber/Yellow
            in 20..39 -> Color.parseColor("#FF9800")   // Orange
            else -> Color.parseColor("#F44336")        // Red
        }
    }

    fun getTextColorForScore(score: Double): Int {
        // For dark backgrounds (red/orange), use white text
        return if (score < 40) {
            Color.WHITE
        } else {
            Color.BLACK
        }
    }
}