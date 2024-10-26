package com.approval.tastebud.fragment



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.approval.tastebud.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.content.Intent
import androidx.appcompat.widget.SearchView
import com.approval.tastebud.data.Recipe
import com.approval.tastebud.pages.Form
import com.approval.tastebud.recyclers.MenuAdapter

class MainPage : Fragment() {

    lateinit var recycler_view: RecyclerView
    lateinit var fab: FloatingActionButton
    lateinit var menuAdapter: MenuAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var search_bar: SearchView
    val recipeList = mutableListOf<Recipe>()
    val filteredList = mutableListOf<Recipe>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_page, container, false)

        recycler_view = view.findViewById(R.id.recycler_view)
        fab = view.findViewById(R.id.fab)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        search_bar = view.findViewById(R.id.search_bar)

        // Setup RecyclerView with adapter and layout manager
        menuAdapter = MenuAdapter(requireContext(), filteredList)
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter = menuAdapter

        // Fetch data from Firestore
        fetchRecipes()

        // Floating Action Button to navigate to form activity
        fab.setOnClickListener {
            val intent = Intent(activity, Form::class.java)
            startActivity(intent)
        }

        // Set up swipe-to-refresh listener
        swipeRefreshLayout.setOnRefreshListener {
            fetchRecipes()  // Refresh the recipes
        }

        // Set up search functionality
        setupSearchView()

        return view
    }

    // Function to fetch data from Firestore
    private fun fetchRecipes() {
        val db = FirebaseFirestore.getInstance()

        db.collection("recipes")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recipes = querySnapshot.toObjects(Recipe::class.java)
                recipeList.clear()
                recipeList.addAll(recipes)
                filteredList.clear()
                filteredList.addAll(recipeList) // Initially, show all recipes
                menuAdapter.updateList(filteredList)

                // Stop the refresh animation after data is fetched
                swipeRefreshLayout.isRefreshing = false
            }
            .addOnFailureListener { exception ->
                // Handle error
                // Stop the refresh animation in case of error
                swipeRefreshLayout.isRefreshing = false
            }
    }

    // Function to filter the recipe list based on search query
    private fun setupSearchView() {
        search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = if (newText.isNullOrEmpty()) {
                    recipeList
                } else {
                    recipeList.filter { it.recipeName.contains(newText, true) }
                }
                filteredList.clear()
                filteredList.addAll(filtered)
                menuAdapter.updateList(filteredList)
                return true
            }
        })
    }
}
