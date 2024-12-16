package com.driven.foodlens.Adapters.Ingredients

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.driven.foodlens.databinding.IngredientsLayoutBinding
import com.driven.foodrecipeapp.ApiSetup.Ingredients.Model.ExtendedIngredient
import com.squareup.picasso.Picasso

class IngredientAdapter : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    class ViewHolder(val binding: IngredientsLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    companion object DiffCallback : DiffUtil.ItemCallback<ExtendedIngredient>() {
        override fun areItemsTheSame(oldItem: ExtendedIngredient, newItem: ExtendedIngredient): Boolean {
            return oldItem.id == newItem.id // Assuming id is unique
        }

        override fun areContentsTheSame(oldItem: ExtendedIngredient, newItem: ExtendedIngredient): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, DiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("Adapter", "Creating ViewHolder for position $viewType")
        return ViewHolder(IngredientsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        val count = differ.currentList.size
        Log.d("AdapterItemCount", "Item count: $count")
        return count
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("BindViewHolder", "onBindViewHolder called for position $position")
        val ingredient = differ.currentList[position]
        Log.d("IngredientBinding", "Position: $position, Name: ${ingredient.name}")

        val quantityString = if (ingredient.amount.toInt() != 0 && ingredient.unit != null) {
            "${ingredient.amount} ${ingredient.unit}"
        } else if (ingredient.amount.toInt() != 0) {
            "${ingredient.amount}"
        } else {
            ""
        }

        holder.binding.apply {
            textView16.text = ingredient.name
            quantity.text = quantityString
            val imageUrl = "https://spoonacular.com/cdn/ingredients_100x100/${ingredient.image}"
            Log.d("ImageUrl", "Loading image URL: $imageUrl")

            Picasso.get().load(imageUrl).into(ingredientVector, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    Log.d("Picasso", "Image loaded successfully: $imageUrl")
                }

                override fun onError(e: Exception?) {
                    Log.e("Picasso", "Failed to load image: $imageUrl", e)
                }
            })
        }
    }
}
