package com.driven.foodrecipeapp.Adapters.SearchAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.driven.foodlens.databinding.RecommendedLayoutBinding
import com.driven.foodrecipeapp.ApiSetup.Model.SearchModel.SearchResult
import com.driven.foodrecipeapp.ItemClickListener.SearchItemListener
import com.squareup.picasso.Picasso

class SearchAdapter(private val listener: SearchItemListener):RecyclerView.Adapter<SearchAdapter.viewHolder>(){

    class viewHolder(val binding: RecommendedLayoutBinding):RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(
            oldItem: SearchResult,
            newItem: SearchResult
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: SearchResult,
            newItem: SearchResult
        ): Boolean {
            return oldItem == newItem
        }

    }

     var differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(RecommendedLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val recipe = differ.currentList[position]

        holder.binding.textView12.text = recipe.title
        holder.binding.imageButton.visibility = View.GONE
        Picasso.get().load(recipe.image).into(holder.binding.recipeImage)

        holder.itemView.setOnClickListener(){
            listener.onSearchItemClick(recipe)
        }
    }

}