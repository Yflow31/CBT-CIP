package com.approval.tastebud.data

data class Recipe(
    val description: String = "",
    val imageUrl: String = "",
    val ingredients: List<Ingredient> = listOf(),
    val name: String = "",
    val phone: String = "",
    val recipeName: String = "",
    val recipeId: String = ""
)

data class Ingredient(
    val name: String = "",
    val quantity: Int = 0 // assuming quantity is an integer
)
