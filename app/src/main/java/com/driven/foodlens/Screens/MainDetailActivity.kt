package com.driven.foodlens.Screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.driven.foodlens.Adapters.Ingredients.IngredientAdapter
import com.driven.foodlens.R
import com.driven.foodlens.databinding.ActivityMainDetailBinding
import com.driven.foodrecipeapp.ApiSetup.Ingredients.ApiObject.IngredientObject
import com.driven.foodrecipeapp.ApiSetup.Ingredients.Factory.IngredientFactory
import com.driven.foodrecipeapp.ApiSetup.Ingredients.Repository.RecipeIngredientRepository
import com.driven.foodrecipeapp.ApiSetup.Ingredients.ViewModel.IngredientsViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.Random

class MainDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainDetailBinding
    private lateinit var viewModel: IngredientsViewModel
    private lateinit var ingredientAdapter: IngredientAdapter

    private var detailId = 0
    private var detailTitle = ""
    private var detailImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        initializeViewModel()
        generateRating()
        setupRecyclerView()
        retrieveIntentData()
        setupViews()
        fetchIngredients()
    }

    private fun generateRating(){
        val rating = Random().nextDouble() * 5.0 + 1.0
        val decimalFormat = DecimalFormat("#.##")
        binding.rating.text = decimalFormat.format(rating)
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializeViewModel() {
        val repository = RecipeIngredientRepository(IngredientObject.api)
        viewModel = ViewModelProvider(this, IngredientFactory(repository)).get(IngredientsViewModel::class.java)
    }

    private fun setupRecyclerView() {
        ingredientAdapter = IngredientAdapter()

        binding.ingredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainDetailActivity)
            adapter = ingredientAdapter
            setHasFixedSize(true)
        }

        viewModel.data.observe(this, Observer { list ->
            Log.d("MainDetailActivity", "Received ingredients list: ${list.extendedIngredients.size}")
            if (list.extendedIngredients.isNotEmpty()) {
                binding.mainprogress.visibility = View.GONE
                binding.ingredientsRecyclerView.visibility = View.VISIBLE
                binding.time.text = "${list.readyInMinutes} mins"
                binding.description.text = list.instructions
                ingredientAdapter.differ.submitList(list.extendedIngredients)
            } else {
                binding.mainprogress.visibility = View.GONE
                binding.ingredientsRecyclerView.visibility = View.GONE
                showToast("No Ingredients Available")
            }
        })
    }

    private fun retrieveIntentData() {
        intent?.let {
            detailId = extractDetailId(it)
            detailTitle = extractDetailTitle(it)
            detailImage = extractDetailImage(it)

            Log.d("MainDetail",detailId.toString())

            if (detailTitle.isEmpty() || detailImage.isEmpty()) {
                showToast("Intent data is incomplete")
            }
        } ?: showToast("Intent is null")
    }

    private fun extractDetailId(intent: Intent): Int {
        return intent.getIntExtra("DN_ID", 0).takeIf { it != 0 }
            ?: intent.getIntExtra("DR_ID", 0).takeIf { it != 0 }
            ?: intent.getIntExtra("DSB_ID", 0).takeIf { it != 0 }
            ?: intent.getIntExtra("DS_ID", 0)
    }

    private fun extractDetailTitle(intent: Intent): String {
        return intent.getStringExtra("DN_Title")
            ?: intent.getStringExtra("DR_Title")
            ?: intent.getStringExtra("DSB_Title")
            ?: intent.getStringExtra("DS_Title")
            ?: ""
    }

    private fun extractDetailImage(intent: Intent): String {
        return intent.getStringExtra("DN_Image")
            ?: intent.getStringExtra("DR_Image")
            ?: intent.getStringExtra("DSB_Image")
            ?: intent.getStringExtra("DS_Image")
            ?: ""
    }

    private fun setupViews() {
        Picasso.get().load(detailImage).into(binding.imageView5)
        binding.dishTitle.text = detailTitle
    }

    private fun fetchIngredients() {
        lifecycleScope.launch {
            try {
                viewModel.getData(detailId)
            } catch (e: Exception) {
                showToast("Error fetching ingredients: ${e.localizedMessage}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}