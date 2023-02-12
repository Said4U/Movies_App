package com.example.movies.view.adapter

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
import com.example.movies.data.movies.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_view_design.view.*
import kotlinx.android.synthetic.main.card_view_design.view.movies_name
import kotlinx.android.synthetic.main.card_view_premiere.view.*

class PremieresAdapter(private val mList: List<Item>?, val mItemClickListener: ItemClickListener) : RecyclerView.Adapter<PremieresAdapter.ViewHolder>() {

    interface ItemClickListener{
        fun onItemClick(id: Int)
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_premiere, parent, false)

        return ViewHolder(view)
    }

    // binds the list videoItems to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(mList?.get(position)?.posterUrl).resize(500, 700).into(holder.imageView)

        holder.itemView.movies_name.textSize = 16F

        var name = mList?.get(position)?.nameRu.toString()
        if (name == "null"){
            name = mList?.get(position)?.nameOriginal.toString()
        }
        if (name.length > 35){
            holder.itemView.movies_name.textSize = 14F
        }
        holder.itemView.movies_name.text = name

        holder.itemView.movies_date.text = mList?.get(position)?.premiereRu
        if (mList?.get(position)?.genres!!.isNotEmpty()){
            holder.itemView.movies_genre_premiere.text = mList[position].genres[0].genre
        }
    }

    // return the number of the videoItems in the list
    override fun getItemCount(): Int {
        return mList?.size ?: 0
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