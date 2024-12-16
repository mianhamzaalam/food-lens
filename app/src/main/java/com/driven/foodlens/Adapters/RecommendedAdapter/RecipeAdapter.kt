package com.driven.foodrecipeapp.Adapters.RecommendedAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.driven.foodlens.databinding.RecommendedLayoutBinding
import com.driven.foodrecipeapp.ApiSetup.Model.Result
import com.driven.foodrecipeapp.ItemClickListener.RecommendListener
import com.squareup.picasso.Picasso

class RecipeAdapter (private val listener: RecommendListener):RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    class ViewHolder(val binding:RecommendedLayoutBinding):RecyclerView.ViewHolder(binding.root)

    private var differCallBack = object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(RecommendedLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val recipe = differ.currentList[position]

        Picasso.get().load(recipe.image).into(holder.binding.recipeImage)
        holder.binding.textView12.text = recipe.title
        holder.binding.imageButton.visibility = View.GONE
        holder.itemView.setOnClickListener(){
            listener.onRecommend(recipe)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}