package com.example.audioanalyzercompanion

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView.Adapter] responsible for displaying a list of [Analysis] results.
 *
 * This adapter binds analysis data to card views, applying dynamic color themes
 * based on the analysis score via [ScoreColorHelper]. It also handles item clicks
 * to navigate to the detailed view of an analysis.
 *
 * @param analyses The collection of [Analysis] items to display in the list.
 * @param onItemClick Lambda function to be executed when an item is selected.
 */
class AnalysisAdapter(
    private val analyses: List<Analysis>,
    private val onItemClick: (Analysis) -> Unit
) : RecyclerView.Adapter<AnalysisAdapter.ViewHolder>() {

    /**
     * ViewHolder for [AnalysisAdapter] that holds references to the UI components
     * of an individual analysis item.
     *
     * @param itemView The inflated view for a single item in the [RecyclerView].
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val idText: TextView = itemView.findViewById(R.id.analysisId)
        val userTrackText: TextView = itemView.findViewById(R.id.userTrack)
        val referenceTrackText: TextView = itemView.findViewById(R.id.referenceTrack)
        val scoreText: TextView = itemView.findViewById(R.id.score)
        val dateText: TextView = itemView.findViewById(R.id.date)
    }

    /**
     * Inflates the layout for an individual analysis item and creates a [ViewHolder].
     *
     * @param parent The [ViewGroup] into which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A new [ViewHolder] that holds the view for an analysis item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_analysis, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds the data from an [Analysis] object to the UI components in the [ViewHolder].
     *
     * This method applies dynamic colors to the [CardView] and [TextView]s based on the
     * analysis score, sets the text content from the analysis data, and attaches a
     * click listener to navigate to the [DetailActivity].
     *
     * @param holder The [ViewHolder] to bind the data to.
     * @param position The position of the [Analysis] item within the dataset.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val analysis = analyses[position]
        val context = holder.itemView.context

        // Get colors based on score
        val cardColor = ScoreColorHelper.getColorForScore(analysis.overall)
        val textColor = ScoreColorHelper.getTextColorForScore(analysis.overall)

        // Apply background color to CardView
        holder.cardView.setCardBackgroundColor(cardColor)

        // Apply text color to all TextViews
        holder.idText.setTextColor(textColor)
        holder.userTrackText.setTextColor(textColor)
        holder.referenceTrackText.setTextColor(textColor)
        holder.scoreText.setTextColor(textColor)
        holder.dateText.setTextColor(textColor)

        // Set text content
        holder.idText.text = context.getString(R.string.analysis_number, analysis.analysis_id)
        holder.userTrackText.text = context.getString(R.string.user_track_label, analysis.user_filename ?: context.getString(R.string.unknown))
        holder.referenceTrackText.text = context.getString(R.string.reference_track_label, analysis.reference_filename ?: context.getString(R.string.unknown))
        holder.scoreText.text = context.getString(R.string.score_label, analysis.overall)

        // Format date safely
        val dateStr = analysis.created_at?.take(10) ?: context.getString(R.string.unknown)
        holder.dateText.text = dateStr

        holder.cardView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("ANALYSIS_ID", analysis.analysis_id)
            context.startActivity(intent)
        }
    }

    /**
     * Returns the total number of items in the analysis list.
     *
     * @return The size of the [analyses] collection.
     */
    override fun getItemCount() = analyses.size
}