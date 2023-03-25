package com.example.movies.view.adapter

import android.graphics.Color
import android.graphics.ColorSpace.Model
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.GenreData
import com.example.movies.data.Video
import com.example.movies.data.VideoItem
import kotlinx.android.synthetic.main.card_view_design.view.*
import kotlinx.android.synthetic.main.genre_row.view.*
import kotlinx.android.synthetic.main.video_card_view_design.view.*


class VideoAdapter(private val videoList: List<VideoItem>?, val mItemClickListener: VideoAdapter.ItemClickListener) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.video_card_view_design, parent, false)
        return VideoViewHolder(view)
    }

    interface ItemClickListener{
        fun onItemClick(url: String)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        if (videoList!![position].name.length > 30){
            holder.itemView.videoName.textSize = 16F
        }
        holder.itemView.videoName.text = videoList[position].name
    }


    override fun getItemCount(): Int {
        return videoList?.size ?: 0
    }

    inner class VideoViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val videoName: TextView

        init {
            videoName = itemView.findViewById(R.id.videoName)
            itemView.setOnClickListener {
                videoList?.get(position)?.url?.let { mItemClickListener.onItemClick(it)}
            }
        }
    }
}