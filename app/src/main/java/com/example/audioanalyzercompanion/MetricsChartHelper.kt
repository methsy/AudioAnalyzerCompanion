package com.example.audioanalyzercompanion

import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

object MetricsChartHelper {

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