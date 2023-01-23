package com.example.movies.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.movies.Film
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_view_design.view.*

class SearchMovieAdapter(private val mList: List<Film>?, val mItemClickListener: ItemClickListener) : RecyclerView.Adapter<SearchMovieAdapter.ViewHolder>() {

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

        val movieMark = mList?.get(position)?.rating

        if (movieMark == "null"){
            holder.itemView.movies_mark.visibility = View.INVISIBLE
        }else{
            holder.itemView.movies_mark.visibility = View.VISIBLE
            holder.itemView.movies_mark.text = movieMark.toString()
            holder.itemView.movies_mark.setTextColor(Color.WHITE)

            when(movieMark!!.toDouble()){
                in 7.0..10.0 -> holder.itemView.movies_mark.setBackgroundColor(Color.parseColor("#32CD32"))
                in 5.0..6.9 -> holder.itemView.movies_mark.setBackgroundColor(Color.GRAY)
                else -> holder.itemView.movies_mark.setBackgroundColor(Color.RED)
            }
        }

        var name = mList?.get(position)?.nameRu.toString()
        if (name == "null"){
            name = mList?.get(position)?.nameEn.toString()
        }
        if (name.length > 35){
            holder.itemView.movies_name.textSize = 14F
        }
        holder.itemView.movies_name.text = name
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
                mList?.get(position)?.filmId?.let { mItemClickListener.onItemClick(it)}
            }
        }
    }
}