package com.driven.foodrecipeapp.Adapters.Alternative

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.driven.foodlens.databinding.IngredientsLayoutBinding
import com.driven.foodrecipeapp.AiModel_Handling.AlternativeRecipe.Model.AiModel.AlternativeIngredient

class AlternativeAdapter : RecyclerView.Adapter<AlternativeAdapter.viewHolder>() {
    class viewHolder(val binding: IngredientsLayoutBinding):RecyclerView.ViewHolder(binding.root)

    private val differCallback = object:DiffUtil.ItemCallback<AlternativeIngredient>(){
        override fun areItemsTheSame(
            oldItem: AlternativeIngredient,
            newItem: AlternativeIngredient
        ): Boolean {
            return newItem==oldItem
        }

        override fun areContentsTheSame(
            oldItem: AlternativeIngredient,
            newItem: AlternativeIngredient
        ): Boolean {
            return newItem==oldItem
        }

    }

    var differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlternativeAdapter.viewHolder {
        return viewHolder(IngredientsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: AlternativeAdapter.viewHolder, position: Int) {
        val ingredient = differ.currentList[position]
        holder.binding.textView16.text = ingredient.name
        holder.binding.quantity.text = ingredient.quantity
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}