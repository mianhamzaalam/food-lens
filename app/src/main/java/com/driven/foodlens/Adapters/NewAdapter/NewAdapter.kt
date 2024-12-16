package com.driven.foodrecipeapp.Adapters.NewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.driven.foodlens.R
import com.driven.foodrecipeapp.ApiSetup.Model.NewRecipe.NewResult
import com.driven.foodrecipeapp.Bookmark_Listener.BookMarkService
import com.driven.foodrecipeapp.ItemClickListener.ViewDetailClickListener
import com.squareup.picasso.Picasso

class NewAdapter(private val list: List<NewResult>,
                 val listener:BookMarkService,
                val listener2:ViewDetailClickListener):RecyclerView.Adapter<NewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recommended_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val recipe = list[position]

        Picasso.get().load(recipe.image).into(holder.image)
        holder.title.text = recipe.title
        holder.bookmark.setOnClickListener(){
            recipe.status = true
            holder.bookmark.isSelected = recipe.status
            listener.addToBookMark(recipe)
        }

        holder.itemView.setOnClickListener(){
            listener2.onViewClick(recipe)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

         var image: ImageView =  itemView.findViewById(R.id.recipeImage)
         var title: TextView = itemView.findViewById(R.id.textView12)
         var bookmark:ImageButton = itemView.findViewById(R.id.imageButton)
    }
}