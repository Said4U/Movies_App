package com.example.movies.view.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.movies.Item
import com.example.movies.data.movies.OneMoviesData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_view_design.view.*

class MoviesDetailAdapter(private val mList: List<OneMoviesData>?, val mItemClickListener: ItemClickListener) : RecyclerView.Adapter<MoviesDetailAdapter.ViewHolder>() {

    interface ItemClickListener{
        fun onItemClick(id: Int)
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list videoItems to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(mList?.get(position)?.posterUrl).resize(500, 700).into(holder.imageView)

        holder.itemView.movies_name.textSize = 16F

        val movieMark = mList?.get(position)?.ratingKinopoisk

        if (movieMark == null){
            holder.itemView.movies_mark.visibility = View.INVISIBLE
        }else{
            holder.itemView.movies_mark.visibility = View.VISIBLE
            holder.itemView.movies_mark.text = movieMark.toString()
            holder.itemView.movies_mark.setTextColor(Color.WHITE)

            when(movieMark){
                in 7.0..10.0 -> holder.itemView.movies_mark.setBackgroundColor(Color.parseColor("#32CD32"))
                in 5.0..6.9 -> holder.itemView.movies_mark.setBackgroundColor(Color.GRAY)
                else -> holder.itemView.movies_mark.setBackgroundColor(Color.RED)
            }
        }
        holder.itemView.movies_name.setTextColor(Color.WHITE)

        var name = mList?.get(position)?.nameRu.toString()
        if (name == "null"){
            name = mList?.get(position)?.nameOriginal.toString()
        }
        if (name.length > 35){
            holder.itemView.movies_name.textSize = 14F
        }
        holder.itemView.movies_name.text = name

        if (mList?.get(position)?.genres!!.isNotEmpty()) {
            holder.itemView.movies_genre.text = mList[position].genres[0].genre
        }
        holder.itemView.movies_year.text = mList[position].year.toString()
    }

    // return the number of the videoItems in the list
    override fun getItemCount(): Int {
        return mList!!.size
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        init {
            ItemView.setOnClickListener {
                mList?.get(position)?.kinopoiskId?.let { mItemClickListener.onItemClick(it)}
            }
        }
    }
}