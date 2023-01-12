package com.example.movies.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.Film
import com.example.movies.data.movies.Item
import com.example.movies.view.HomeFragment
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

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(mList?.get(position)?.posterUrl).resize(500, 700).into(holder.imageView)

        holder.itemView.movies_name.textSize = 16F

        var name = mList?.get(position)?.nameRu.toString()
        if (name == "null"){
            name = mList?.get(position)?.nameEn.toString()
        }
        if (name.length > 35){
            holder.itemView.movies_name.textSize = 14F
        }
        holder.itemView.movies_name.text = name
    }

    // return the number of the items in the list
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