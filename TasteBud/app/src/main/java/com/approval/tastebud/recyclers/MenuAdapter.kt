package com.approval.tastebud.recyclers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.approval.tastebud.R
import com.approval.tastebud.data.Recipe
import com.approval.tastebud.pages.RecipeDetailActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

// Adapter class to bind Recipe data to RecyclerView
class MenuAdapter(
    private val context: Context,
    private var recipeList: List<Recipe>
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    // ViewHolder class to hold views for each recipe item
    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeNameTextView: TextView = itemView.findViewById(R.id.recipeName)
        val your_Name: TextView = itemView.findViewById(R.id.your_Name)
        val recipeView: Button = itemView.findViewById(R.id.recipeView)
        val recipeImageView: ImageView = itemView.findViewById(R.id.recipeImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val recipe = recipeList[position]

        // Bind recipe name and description
        holder.recipeNameTextView.text = recipe.recipeName
        holder.your_Name.text = recipe.name

        // Load image from Firebase Storage using the imageUrl
        loadRecipeImage(recipe.imageUrl, holder.recipeImageView)

        holder.recipeView.setOnClickListener {
            // Create an Intent to open RecipeDetailActivity
            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra("recipeId",recipe.recipeId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    // Load the image from Firebase Storage using Glide
    private fun loadRecipeImage(imageUrl: String, imageView: ImageView) {
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)

        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(context)
                .load(uri)
                .into(imageView)
        }.addOnFailureListener {
            // Handle image loading failure if needed
        }
    }



    // Update the adapter's list when new data is provided
    fun updateList(newList: List<Recipe>) {
        recipeList = newList
        notifyDataSetChanged()
    }
}
