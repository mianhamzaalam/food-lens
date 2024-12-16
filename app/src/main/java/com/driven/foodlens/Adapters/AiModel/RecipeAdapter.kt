import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.driven.foodlens.AiModel_Handling.ImagePrediction.Model.Recipe
import com.driven.foodlens.R

class RecipeAdapter(private val recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientsTextView: TextView = itemView.findViewById(R.id.textView16)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantity)

        fun bind(recipe: Recipe) {
            ingredientsTextView.text = recipe.recipe_name
            ingredientsTextView.text = recipe.ingredients.joinToString(", ")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredients_layout, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
        holder.quantityTextView.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}
