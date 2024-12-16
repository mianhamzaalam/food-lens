package com.driven.foodrecipeapp.Adapters.BookMarkAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.driven.foodlens.databinding.RecommendedLayoutBinding
import com.driven.foodrecipeapp.BookMark_Implementation.BookMarkDataBase.BookMarkDatabase
import com.driven.foodrecipeapp.BookMark_Implementation.Entity.BookMark_Recipe
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookMarkAdapter(
    private val context: Context,
    private val onItemCountChanged: (Int) -> Unit // Callback for item count changes
) : RecyclerView.Adapter<BookMarkAdapter.viewHolder>() {

    class viewHolder(val binding: RecommendedLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<BookMark_Recipe>() {
        override fun areItemsTheSame(oldItem: BookMark_Recipe, newItem: BookMark_Recipe): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BookMark_Recipe, newItem: BookMark_Recipe): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(RecommendedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val recipe = differ.currentList[position]

        holder.binding.imageButton.isSelected = recipe.state
        holder.binding.textView12.text = recipe.title
        Picasso.get().load(recipe.image).into(holder.binding.recipeImage)

        holder.binding.imageButton.setOnClickListener {
            if (recipe.state) {
                recipe.state = false
                holder.binding.imageButton.isSelected = recipe.state
                removeRecipeFromDatabase(recipe, position)
            } else {
                Log.d("Book", "Nothing to Do")
            }
        }
    }

    private fun removeRecipeFromDatabase(recipe: BookMark_Recipe, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = BookMarkDatabase.getDataBase(context).dao()
            db.delete(recipe) // Assuming you have a DAO method for deletion

            CoroutineScope(Dispatchers.Main).launch {
                val currentList = differ.currentList.toMutableList()
                if (position >= 0 && position < currentList.size) {
                    currentList.removeAt(position)
                    differ.submitList(currentList)

                    // Notify fragment of the new item count
                    onItemCountChanged(currentList.size)
                } else {
                    Log.e("BookMarkAdapter", "Invalid index: $position for size: ${currentList.size}")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
