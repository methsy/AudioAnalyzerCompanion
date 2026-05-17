package com.example.audioanalyzercompanion

import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

/**
 * Helper object for configuring and populating [BarChart] instances using the MPAndroidChart library.
 *
 * This utility provides standardized configuration for charts used to compare audio metrics
 * between a user track and a reference track.
 */
object MetricsChartHelper {

    /**
     * Configures the visual styling and behavior of a [BarChart].
     *
     * This method sets up the X and Y axes, disables unnecessary components like the legend
     * and right axis, and applies standardized offsets and animations for a consistent UI.
     *
     * @param chart The [BarChart] instance to be configured.
     */
    fun setupChart(chart: BarChart) {
        chart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setPinchZoom(false)
            setScaleEnabled(false)
            setTouchEnabled(false)  // Disable touch for cleaner look

            // X-axis configuration
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textSize = 12f
                granularity = 1f
                setLabelCount(4)
                yOffset = 8f  // Space between axis and labels
            }

            // Y-axis configuration
            axisLeft.apply {
                setDrawGridLines(true)
                textSize = 11f
                axisMinimum = 0f
                setLabelCount(5)
            }
            axisRight.isEnabled = false

            // Legend - disable since we have custom legend in layout
            legend.isEnabled = false

            // Extra space
            extraBottomOffset = 20f
            extraTopOffset = 20f
            extraLeftOffset = 20f
            extraRightOffset = 20f

            // Animation
            animateY(1000)
        }
    }

    /**
     * Populates the [BarChart] with data comparing user metrics against reference metrics.
     *
     * This method creates two datasets (User and Reference), groups them side-by-side for each
     * metric, applies custom color schemes, and formats the value labels. It also configures
     * the X-axis labels and ensures the chart is refreshed and positioned correctly.
     *
     * @param chart The [BarChart] to populate with data.
     * @param userValues A list of floating-point values representing the user's audio metrics.
     * @param referenceValues A list of floating-point values representing the reference audio metrics.
     * @param labels A list of strings used as labels for the X-axis (e.g., metric names).
     */
    fun setChartData(
        chart: BarChart,
        userValues: List<Float>,
        referenceValues: List<Float>,
        labels: List<String>
    ) {
        // Create entries for both datasets
        val userEntries = ArrayList<BarEntry>()
        val referenceEntries = ArrayList<BarEntry>()

        for (i in userValues.indices) {
            userEntries.add(BarEntry(i.toFloat(), userValues[i]))
            referenceEntries.add(BarEntry(i.toFloat(), referenceValues[i]))
        }

        // Create datasets
        val userDataSet = BarDataSet(userEntries, "Your Track").apply {
            color = Color.parseColor("#2196F3")
            valueTextColor = Color.BLACK
            valueTextSize = 11f
            setDrawValues(true)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return String.format("%.1f", value)
                }
            }
        }

        val referenceDataSet = BarDataSet(referenceEntries, "Reference").apply {
            color = Color.parseColor("#FF9800")
            valueTextColor = Color.BLACK
            valueTextSize = 11f
            setDrawValues(true)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return String.format("%.1f", value)
                }
            }
        }

        // Combine into BarData
        val data = BarData(userDataSet, referenceDataSet)

        // Bar dimensions
        val groupSpace = 0.4f    // Space between groups
        val barSpace = 0.05f     // Space between bars in same group
        val barWidth = 0.35f     // Width of each bar

        data.barWidth = barWidth

        chart.data = data

        // Configure X-axis with labels
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        chart.xAxis.setLabelCount(labels.size)

        // Group the bars
        data.groupBars(0f, groupSpace, barSpace)

        // Set visible range to show all bars properly
        chart.setVisibleXRangeMaximum(5f)
        chart.moveViewToX(0f)

        // Refresh chart
        chart.invalidate()
    }
}