package com.example.movies.view.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.red
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.movies.Film
import com.example.movies.data.movies.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_view_design.view.*
import kotlinx.android.synthetic.main.top_card_design.view.*

class TopAdapter(private val mList: List<Film>?, val mItemClickListener: ItemClickListener) : RecyclerView.Adapter<TopAdapter.ViewHolder>() {

    interface ItemClickListener{
        fun onItemClick(id: Int)
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.top_card_design, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(mList?.get(position)?.posterUrl).fit().into(holder.imageView)


        var movieMark = mList?.get(position)?.rating

        if (movieMark == null){
            holder.itemView.top_movies_mark.visibility = View.INVISIBLE
        }
        else{
            holder.itemView.top_movies_mark.visibility = View.VISIBLE
            holder.itemView.top_movies_mark.setTextColor(Color.WHITE)

            if (movieMark.contains("%")){
                if (movieMark.length > 4){
                    movieMark = movieMark.substring(0, movieMark.length - 3)
                }else{
                    movieMark = movieMark.substring(0, movieMark.length - 1)
                }
                when(movieMark.toInt()){
                    in 70..100 -> holder.itemView.top_movies_mark.setBackgroundColor(Color.parseColor("#32CD32"))
                    in 50..69 -> holder.itemView.top_movies_mark.setBackgroundColor(Color.GRAY)
                    else -> holder.itemView.top_movies_mark.setBackgroundColor(Color.RED)
                }
                movieMark = "$movieMark%"

            }else{
                when(movieMark.toDouble()){
                    in 7.0..10.0 -> holder.itemView.top_movies_mark.setBackgroundColor(Color.parseColor("#32CD32"))
                    in 5.0..6.9 -> holder.itemView.top_movies_mark.setBackgroundColor(Color.GRAY)
                    else -> holder.itemView.top_movies_mark.setBackgroundColor(Color.RED)
                }
            }
            holder.itemView.top_movies_mark.text = movieMark.toString()

        }


        var name = mList?.get(position)?.nameRu.toString()
        if (name == "null"){
            name = mList?.get(position)?.nameEn.toString()
        }

        holder.itemView.top_movies_name.setTextColor(Color.WHITE)

        holder.itemView.top_movies_name.text = name
        mList?.get(position)?.genres?.let {
            holder.itemView.top_movies_genre.text = it[0].genre
            if(it.size > 1){
                holder.itemView.top_movies_genre.text = it[0].genre + " , " + it[1].genre
            }
        }
        mList?.get(position)?.countries?.let {
            holder.itemView.top_movies_country_year.text = it[0].country + " • " + mList[position].year
            if(it.size > 1){
                holder.itemView.top_movies_country_year.text = it[0].country + ", " + it[1].country + " • " + mList[position].year
            }
        }
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }


    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.topCardImageView)
        init {
            ItemView.setOnClickListener {
                mList?.get(position)?.filmId?.let { mItemClickListener.onItemClick(it)}
            }
        }
    }
}