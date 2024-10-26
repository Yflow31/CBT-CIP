package com.approval.tastebud.pages

import IngredientAdapter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.approval.tastebud.R
import com.approval.tastebud.data.Ingredients
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class Form : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var yourNameEditText: EditText
    private lateinit var recipeNameEditText: EditText
    private lateinit var descriptionInputLayout: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var rc_recipe_name: RecyclerView
    private lateinit var ingredientAddButton: Button

    private lateinit var ingredientList: MutableList<Ingredients>
    private lateinit var ingredientAdapter: IngredientAdapter

    private lateinit var et_ingredientName: EditText
    private lateinit var et_ingredientQuantity: EditText

    private lateinit var imgView_recipe: ImageView
    private lateinit var recipeImageAdd_button: Button
    private lateinit var save_recipe_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form)

        titleTextView = findViewById(R.id.Title)
        yourNameEditText = findViewById(R.id.et_your_name)
        recipeNameEditText = findViewById(R.id.et_recipe_name)
        descriptionInputLayout = findViewById(R.id.description)
        phoneNumberEditText = findViewById(R.id.et_phone_number)
        rc_recipe_name = findViewById(R.id.rc_recipe_name)
        ingredientAddButton = findViewById(R.id.ingredient_add_button)

        ingredientList = mutableListOf()
        ingredientAdapter = IngredientAdapter(ingredientList)

        rc_recipe_name.layoutManager = LinearLayoutManager(this)
        rc_recipe_name.adapter = ingredientAdapter

        et_ingredientName = findViewById(R.id.et_ingredientName)
        et_ingredientQuantity = findViewById(R.id.et_ingredientQuantity)

        imgView_recipe = findViewById(R.id.imgView_recipe)
        recipeImageAdd_button = findViewById(R.id.recipeImageAdd_button)
        save_recipe_button = findViewById(R.id.save_recipe_button)

        imgView_recipe.visibility = ImageView.GONE

        val singlePhotoPickerLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imgView_recipe.setImageURI(uri)
                imgView_recipe.visibility = ImageView.VISIBLE
                imgView_recipe.tag = uri
            }
        }

        recipeImageAdd_button.setOnClickListener {
            singlePhotoPickerLauncher.launch(
                 PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }


        ingredientAddButton.setOnClickListener {
            val ingredientName = et_ingredientName.text.toString()
            val ingredientQuantity = et_ingredientQuantity.text.toString()

            if (ingredientName.isNotEmpty() && ingredientQuantity.isNotEmpty()) {

                val quantity = ingredientQuantity.toIntOrNull()

                if (quantity != null && quantity > 0) {
                    val ingredient = Ingredients(ingredientName, quantity)
                    ingredientList.add(ingredient)
                    ingredientAdapter.notifyItemInserted(ingredientList.size - 1)

                    et_ingredientName.text.clear()
                    et_ingredientQuantity.text.clear()
                } else {
                    et_ingredientQuantity.error = "Please enter a valid quantity"
                }
            } else {
                if (ingredientName.isEmpty()) {
                    et_ingredientName.error = "Please enter an ingredient name"
                }
                if (ingredientQuantity.isEmpty()) {
                    et_ingredientQuantity.error = "Please enter a quantity"
                }
            }
        }

        save_recipe_button.setOnClickListener {
            val name = yourNameEditText.text.toString()
            val phone = phoneNumberEditText.text.toString()
            val recipeName = recipeNameEditText.text.toString()

            if (name.isEmpty() || phone.isEmpty() || recipeName.isEmpty()) {

            } else {
                val uri = imgView_recipe.tag as? Uri
                if (uri != null) {
                    uploadImageToFirebase(uri)
                }
            }
        }
    }

    private fun uploadImageToFirebase(uri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveRecipeToFirestore(downloadUri.toString())

                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveRecipeToFirestore(imageUrl: String) {
        val firestore = FirebaseFirestore.getInstance()

        // Generate a unique document ID for the recipe
        val documentId = firestore.collection("recipes").document().id

        val recipeData = hashMapOf(
            "name" to yourNameEditText.text.toString(),
            "phone" to phoneNumberEditText.text.toString(),
            "recipeName" to recipeNameEditText.text.toString(),
            "description" to descriptionInputLayout.text.toString(),
            "imageUrl" to imageUrl, // Use the image URL from Firebase Storage
            "ingredients" to ingredientList.map {
                mapOf("name" to it.ingredients_name, "quantity" to it.ingredients_quantity)
            },
            "recipeId" to documentId // Add the recipe ID
        )

        firestore.collection("recipes").document(documentId)
            .set(recipeData)
            .addOnSuccessListener {
                Toast.makeText(this, "Recipe saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to save recipe", Toast.LENGTH_SHORT).show()
                Log.e("FirestoreError", "Error saving recipe", exception)
            }
    }
}
