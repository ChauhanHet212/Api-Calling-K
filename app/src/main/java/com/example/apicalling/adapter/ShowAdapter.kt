package com.example.apicalling.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apicalling.InfoActivity
import com.example.apicalling.R
import com.example.apicalling.model.Show
import com.squareup.picasso.Picasso

class ShowAdapter(val context: Context, var showList: List<Show>) : RecyclerView.Adapter<ShowAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.show_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Picasso.get().load(showList.get(position).image.original).placeholder(R.drawable.placeholder).into(holder.img)
        } catch (e: Exception) {
            holder.img.setImageResource(R.drawable.placeholder)
        }
        holder.name.setText(showList.get(position).name)
        holder.language.setText(showList.get(position).language)

        holder.itemView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(context, InfoActivity::class.java)
                intent.putExtra("data", showList.get(holder.adapterPosition))
                context.startActivity(intent)
            }
        })
    }

    override fun getItemCount(): Int {
        return showList.size
    }

    fun filterShow(shows: List<Show>){
        showList = shows
        notifyDataSetChanged()
    }


    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.show_img)
        val name: TextView = itemView.findViewById(R.id.show_name)
        val language: TextView = itemView.findViewById(R.id.show_language)
    }
}
