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
import com.example.movies.data.ItemSimilar
import com.example.movies.data.movies.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_view_design.view.*
import kotlinx.android.synthetic.main.card_view_design.view.movies_name
import kotlinx.android.synthetic.main.card_view_premiere.view.*
import kotlinx.android.synthetic.main.favorites_pref_card_view.view.*
import kotlinx.android.synthetic.main.top_card_design.view.*

class FavoritesPreferencesAdapter(private val mList: List<ItemSimilar>?, val mItemClickListener: ItemClickListener) : RecyclerView.Adapter<FavoritesPreferencesAdapter.ViewHolder>() {

    interface ItemClickListener{
        fun onItemClick(id: Int)
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorites_pref_card_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list videoItems to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(mList?.get(position)?.posterUrl).fit().into(holder.imageView)

        holder.itemView.movies_name_fav_pref.textSize = 14F

        holder.itemView.movies_name_fav_pref.setTextColor(Color.WHITE)

        var name = mList?.get(position)?.nameRu.toString()
        if (name == "null"){
            name = mList?.get(position)?.nameOriginal.toString()
        }
        if (name.length > 35){
            holder.itemView.movies_name_fav_pref.textSize = 12F
        }
        holder.itemView.movies_name_fav_pref.text = name

    }

    // return the number of the videoItems in the list
    override fun getItemCount(): Int {
        return mList!!.size
    }


    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.fav_pref_imageView)
        init {
            ItemView.setOnClickListener {
                mList?.get(position)?.filmId?.let { mItemClickListener.onItemClick(it)}
            }
        }
    }
}