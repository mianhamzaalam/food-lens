package com.driven.foodrecipeapp.Adapters.SeasonAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.driven.foodlens.R
import com.driven.foodrecipeapp.ApiSetup.Model.Seasonal.SeasonResult
import com.squareup.picasso.Picasso

class SeasonAdapter(private val list:List<SeasonResult>):
    RecyclerView.Adapter<SeasonAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recommended_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val recipe = list[position]

        Picasso.get().load(recipe.image).into(holder.image)
        holder.title.text = recipe.title
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

         var image: ImageView = itemView.findViewById(R.id.recipeImage)
         var title: TextView = itemView.findViewById(R.id.textView12)

    }
}