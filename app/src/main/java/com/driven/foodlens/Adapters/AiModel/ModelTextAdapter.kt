import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.driven.foodlens.AiModel_Handling.ImagePrediction.Model.Recipe
import com.driven.foodlens.R

class ModelTextAdapter(private val recipeNames: List<Recipe>) :
    RecyclerView.Adapter<ModelTextAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeNameTextView: TextView = itemView.findViewById(R.id.textView19)
        // val quantityTextView: TextView = itemView.findViewById(R.id.quantity)

        fun bind(recipe: Recipe) {
            recipeNameTextView.text = recipe.recipe_name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipename_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModelTextAdapter.ViewHolder, position: Int) {
        holder.bind(recipeNames[position])
        // holder.quantityTextView.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return recipeNames.size
    }
}
