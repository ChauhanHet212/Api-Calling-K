package com.example.apicalling.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalling.R
import com.example.apicalling.model.Episode
import com.squareup.picasso.Picasso

class EpisodeAdapter(val context: Context, var episodeList: List<Episode>) : RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.episode_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Picasso.get().load(episodeList.get(position).image.original).placeholder(R.drawable.placeholder).into(holder.ep_img)
        } catch (e: Exception) {
            holder.ep_img.setImageResource(R.drawable.placeholder)
        }
        holder.ep_name.setText(Html.fromHtml("<b>Name :- </b>${episodeList.get(position).name}"))
        holder.ep_season.setText(Html.fromHtml("<b>Season :- </b>${episodeList.get(position).season}"))
        holder.ep_episode.setText(Html.fromHtml("<b>Episode :- </b>${episodeList.get(position).number}"))
        holder.ep_run_time.setText(Html.fromHtml("<b>Run Time :- </b>${episodeList.get(position).runtime}"))
        holder.ep_rating.setText(Html.fromHtml("<b>Rating :- </b>${episodeList.get(position).rating.average}"))
        try {
            holder.ep_about.setText(Html.fromHtml(episodeList.get(position).summary))
        } catch (e: Exception) {
            holder.ep_about.setText("-")
        }
    }

    override fun getItemCount(): Int {
        return episodeList.size
    }

    fun change(changedList: List<Episode>) {
        episodeList = changedList
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ep_img: ImageView = itemView.findViewById(R.id.ep_img)
        val ep_name: TextView = itemView.findViewById(R.id.ep_name)
        val ep_season: TextView = itemView.findViewById(R.id.ep_season)
        val ep_episode: TextView = itemView.findViewById(R.id.ep_episode)
        val ep_run_time: TextView = itemView.findViewById(R.id.ep_run_time)
        val ep_rating: TextView = itemView.findViewById(R.id.ep_rating)
        val ep_about: TextView = itemView.findViewById(R.id.ep_about)
    }
}