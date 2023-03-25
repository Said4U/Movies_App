package com.example.movies.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.ItemAward
import com.example.movies.data.VideoItem
import kotlinx.android.synthetic.main.award_card_view_design.view.*
import kotlinx.android.synthetic.main.video_card_view_design.view.*
import kotlinx.android.synthetic.main.video_card_view_design.view.videoCardView


class AwardsAdapter(private val awardsList: List<ItemAward>?) :
    RecyclerView.Adapter<AwardsAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.award_card_view_design, parent, false)
        return VideoViewHolder(view)
    }


    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        if (awardsList!![position].name.length > 40){
            holder.itemView.awardName.textSize = 16F
        }
        var awardsString = awardsList[position].name + ": " + awardsList[position].nominationName

        awardsString += if (awardsList[position].win) {
            holder.itemView.videoCardView.setCardBackgroundColor(Color.parseColor("#DAA520"))
            " (Победа)"
        } else{
            holder.itemView.videoCardView.setCardBackgroundColor(Color.parseColor("#A9A9A9"))
            " (Номинация)"

        }
        holder.itemView.awardName.text = awardsString

    }


    override fun getItemCount(): Int {
        return awardsList?.size ?: 0
    }

    inner class VideoViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val awardName: TextView

        init {
            awardName = itemView.findViewById(R.id.awardName)
        }
    }
}