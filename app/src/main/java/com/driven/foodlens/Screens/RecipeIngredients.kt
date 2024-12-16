package com.driven.foodlens.Screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.driven.foodlens.Adapters.AiModel.DirectionAdapter
import com.driven.foodlens.R
import com.driven.foodlens.databinding.ActivityRecipeIngredientsBinding
import com.driven.foodrecipeapp.Adapters.Alternative.AlternativeAdapter
import com.driven.foodrecipeapp.AiModel_Handling.AlternativeRecipe.Model.AiModel.AlternativeRecipe
import com.driven.foodrecipeapp.AiModel_Handling.AlternativeRecipe.Model.AlternativeRecipeData
import com.driven.foodrecipeapp.AiModel_Handling.AlternativeRecipe.Object.AlternativeModelObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeIngredients : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeIngredientsBinding
    private lateinit var adapterRecipeIngredients: AlternativeAdapter
    private lateinit var adapterDirection: DirectionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        retrieveIntentData()
    }

    private fun setupRecyclerView() {
        adapterRecipeIngredients = AlternativeAdapter()
        adapterDirection = DirectionAdapter()

        binding.ingredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RecipeIngredients)
            adapter = adapterRecipeIngredients
        }

        binding.Directions.apply {
            layoutManager = LinearLayoutManager(this@RecipeIngredients)
            adapter = adapterDirection
        }
    }

    private fun retrieveIntentData() {
        val recipeName = intent.getStringExtra("AllergicRecipe") ?: ""
        val dietary = intent.getStringExtra("AllergicDiet") ?: ""
        val allergyList = intent.getStringArrayListExtra("Allergic") ?: arrayListOf()

        Log.d("Ingredient", recipeName)
        Log.d("IngredientD", dietary)
        Log.d("IngredientA", allergyList.toString())

        binding.textView21.text = recipeName

        val token = getUserToken()
        Log.d("RecipeIngredients", "Token in retrieveIntentData: $token")

        if (token != null) {
            Log.d("RecipeIngredients", "Token retrieved from SharedPreferences: $token")
            getAlternativeRecipeIngredients(AlternativeRecipeData(recipeName, dietary, allergyList), token)
        } else {
            Log.d("RecipeIngredients", "Token is null or empty")
        }
    }

    private fun getUserToken(): String? {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        return sharedPreferences.getString("UserToken", null)
    }

    private fun getAlternativeRecipeIngredients(data: AlternativeRecipeData, userToken: String) {
        AlternativeModelObject.api.getAlternatives(userToken, data).enqueue(object :
            Callback<AlternativeRecipe?> {
            override fun onResponse(call: Call<AlternativeRecipe?>, response: Response<AlternativeRecipe?>) {
                if (response.isSuccessful) {

                    response.body()?.Response?.alternative_directions?.let { list ->
                        if (list.isNotEmpty()) {
                            adapterDirection.differ.submitList(list)
                        } else {
                            Toast.makeText(this@RecipeIngredients, "Directions Not Available", Toast.LENGTH_SHORT).show()
                        }
                    }

                    response.body()?.Response?.alternative_ingredients?.let { list ->
                        if (list.isNotEmpty()) {
                            adapterRecipeIngredients.differ.submitList(list)
                            binding.ingredientsRecyclerView.visibility = View.VISIBLE
                            binding.dataAlert.visibility = View.GONE
                            Log.d("RecyclerView", "List submitted with size: ${list.size}")
                        } else {
                            showEmptyState()
                        }
                    } ?: showEmptyState()
                } else {
                    Log.d("API Response", "Unsuccessful response: ${response.code()}")
                    showEmptyState()
                }
            }

            override fun onFailure(call: Call<AlternativeRecipe?>, t: Throwable) {
                Toast.makeText(this@RecipeIngredients, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showEmptyState() {
        binding.ingredientsRecyclerView.visibility = View.GONE
        binding.dataAlert.visibility = View.VISIBLE
    }
}