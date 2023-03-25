package com.example.movies.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.ItemAward
import com.example.movies.data.PersonItem
import com.example.movies.data.VideoItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.award_card_view_design.view.*
import kotlinx.android.synthetic.main.person_card_view_design.view.*
import kotlinx.android.synthetic.main.video_card_view_design.view.*
import kotlinx.android.synthetic.main.video_card_view_design.view.videoCardView


class PersonAdapter(private val personList: List<PersonItem>?) :
    RecyclerView.Adapter<PersonAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.person_card_view_design, parent, false)
        return VideoViewHolder(view)
    }


    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        if (personList!![position].nameRu.length > 30){
            holder.itemView.personName.textSize = 16F
        }
        holder.itemView.personName.text = personList[position].nameRu

        val role = personList[position].professionText
        holder.itemView.personRole.text = role.subSequence(0, role.length - 1)

        Picasso.get().load(personList[position].posterUrl).fit().into(holder.itemView.imageViewPerson)


    }


    override fun getItemCount(): Int {
        return personList?.size ?: 0
    }

    inner class VideoViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val personName: TextView
        private val imageView: ImageView

        init {
            personName = itemView.findViewById(R.id.personName)
            imageView = itemView.findViewById(R.id.imageViewPerson)
        }
    }
}