package com.example.apicalling.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalling.R
import com.example.apicalling.model.Cast
import com.squareup.picasso.Picasso

class CastAdapter(val context: Context, var castList: List<Cast>) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.cast_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Picasso.get().load(castList.get(position).person.image.original).placeholder(R.drawable.placeholder).into(holder.cast_img)
        } catch (e: Exception) {
            holder.card1.visibility = View.GONE
        }
        try {
            Picasso.get().load(castList.get(position).character.image.original).placeholder(R.drawable.placeholder).into(holder.character_img)
        } catch (e: Exception) {
            holder.card2.visibility = View.GONE
        }

        holder.cast_name.setText(castList.get(position).person.name)
        holder.character_name.setText(castList.get(position).character.name)
    }

    override fun getItemCount(): Int {
        return castList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card1: CardView = itemView.findViewById(R.id.card1)
        val card2: CardView = itemView.findViewById(R.id.card2)
        val cast_img: ImageView = itemView.findViewById(R.id.cast_img)
        val character_img: ImageView = itemView.findViewById(R.id.character_img)
        val cast_name: TextView = itemView.findViewById(R.id.cast_name)
        val character_name: TextView = itemView.findViewById(R.id.character_name)
    }
}