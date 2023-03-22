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
import kotlinx.android.synthetic.main.genre_row.view.*


class GenreAdapter(private val modelList: List<GenreData>?) :
    RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.genre_row, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val model = modelList!![position]
        holder.itemView.genre_name.text = model.text
        holder.itemView.setBackgroundResource(if (model.isSelected) R.drawable.corners_genres_select else R.drawable.corners_genres)
        holder.itemView.genre_name.setTextColor(if (model.isSelected) Color.parseColor("#F5DEB3") else Color.WHITE)

        holder.itemView.genre_name.setOnClickListener {
            model.isSelected = !model.isSelected
            holder.itemView.setBackgroundResource(if (model.isSelected) R.drawable.corners_genres_select else R.drawable.corners_genres)
            holder.itemView.genre_name.setTextColor(if (model.isSelected) Color.parseColor("#F5DEB3") else Color.WHITE)
        }
    }


    override fun getItemCount(): Int {
        return modelList?.size ?: 0
    }

    inner class GenreViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val view: View
        private val genreName: TextView

        init {
            view = itemView
            genreName = itemView.findViewById(R.id.genre_name)
        }
    }
}