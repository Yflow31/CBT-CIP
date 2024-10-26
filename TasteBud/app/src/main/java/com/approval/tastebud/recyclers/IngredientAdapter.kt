import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.approval.tastebud.R
import com.approval.tastebud.data.Ingredients

class IngredientAdapter(private val ingredients: MutableList<Ingredients>): RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientNameEditText: EditText = itemView.findViewById(R.id.ingredientName)
        val ingredientQuantityEditText: EditText = itemView.findViewById(R.id.ingredientQuantity)
        val deleteButton: View = itemView.findViewById(R.id.deleteButton)

        fun bind(ingredient: Ingredients) {
            ingredientNameEditText.setText(ingredient.ingredients_name)
            ingredientQuantityEditText.setText(ingredient.ingredients_quantity.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item_list, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {

        val ingredient = ingredients[position]
        holder.bind(ingredient)


        holder.deleteButton.setOnClickListener {
            ingredients.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, ingredients.size)
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }
}
