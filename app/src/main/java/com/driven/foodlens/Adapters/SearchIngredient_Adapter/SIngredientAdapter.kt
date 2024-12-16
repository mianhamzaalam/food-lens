package com.driven.foodrecipeapp.Adapters.SearchIngredient_Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.driven.foodlens.R
import com.driven.foodrecipeapp.ApiSetup.Model.SearchRecipeByIngredients.SearchRecipeIngredientModelItem
import com.driven.foodrecipeapp.ItemClickListener.SearchByIngredientListener
import com.squareup.picasso.Picasso

class SIngredientAdapter(private var list:List<SearchRecipeIngredientModelItem>,private val listener: SearchByIngredientListener): RecyclerView.Adapter<SIngredientAdapter.viewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SIngredientAdapter.viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recommended_layout,parent,false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SIngredientAdapter.viewHolder, position: Int) {

        val recipe = list[position]
        holder.bookmark.visibility = View.GONE

        Picasso.get().load(recipe.image).into(holder.image)
        holder.title.text = recipe.title

        holder.itemView.setOnClickListener(){
            listener.onSearchIngredientViewClick(recipe)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(newList: List<SearchRecipeIngredientModelItem>) {
        list = newList
        notifyDataSetChanged()  // Notify the adapter that data has changed
    }

    inner class viewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var image: ImageView =  itemView.findViewById(R.id.recipeImage)
        var title: TextView = itemView.findViewById(R.id.textView12)
        var bookmark: ImageButton = itemView.findViewById(R.id.imageButton)
    }
}