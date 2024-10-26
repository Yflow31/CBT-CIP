package com.approval.tastebud.pages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.approval.tastebud.R
import com.approval.tastebud.data.Recipe
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var recipeImage: ImageView
    private lateinit var tv_recipeName: TextView
    private lateinit var tv_yourName: TextView
    private lateinit var tv_DescriptionDetail: TextView

    private lateinit var ingredientsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_detail)

        // Initialize UI components
        recipeImage = findViewById(R.id.recipeImage)
        tv_recipeName = findViewById(R.id.tv_recipeName)
        tv_DescriptionDetail = findViewById(R.id.tv_DescriptionDetail)
        ingredientsContainer = findViewById(R.id.ingredientsContainer)
        tv_yourName = findViewById(R.id.tv_yourName)

        // Get the recipeId from the intent
        val recipeId = intent.getStringExtra("recipeId")

        if (recipeId != null) {
            fetchRecipeFromFirestore(recipeId)
        } else {
            // Handle the case where recipeId is not provided
            Toast.makeText(this, "Error: No recipe ID found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchRecipeFromFirestore(recipeId: String) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("recipes").document(recipeId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Map the document to the Recipe data class
                    val recipe = document.toObject(Recipe::class.java)

                    if (recipe != null) {
                        // Populate the UI with the fetched recipe data

                        // Load the recipe image using Glide (or any other image loading library)
                        Glide.with(this)
                            .load(recipe.imageUrl)
                            .into(recipeImage)

                        // Set the recipe details in the TextViews
                        tv_recipeName.text = recipe.recipeName
                        tv_DescriptionDetail.text = recipe.description
                        tv_yourName.text = recipe.name

                        Log.d("RecipeDetailActivity", "Number of ingredients: ${recipe.ingredients.size}")


                        // Dynamically add ingredients to the LinearLayout
                        recipe.ingredients.forEach { ingredient ->
                            val ingredientView = LayoutInflater.from(this)
                                .inflate(R.layout.item_ingredient, ingredientsContainer, false)

                            val ingredientName = ingredientView.findViewById<TextView>(R.id.tv_ingredientName)
                            val ingredientQuantity = ingredientView.findViewById<TextView>(R.id.tv_ingredientQuantity)

                            ingredientName.text = ingredient.name
                            ingredientQuantity.text = ingredient.quantity.toString()

                            ingredientsContainer.addView(ingredientView)
                        }
                    }
                } else {
                    // Handle the case where no document was found
                    Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching recipe", Toast.LENGTH_SHORT).show()
            }
    }
}


