package com.driven.foodlens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.driven.foodlens.databinding.FragmentSearchByIngredientsBinding
import com.driven.foodrecipeapp.ActionClass.SearchByIngredients.SBIngredients
import com.driven.foodrecipeapp.Adapters.SearchIngredient_Adapter.SIngredientAdapter
import com.driven.foodrecipeapp.ApiSetup.Factory.ViewModelFactory
import com.driven.foodrecipeapp.ApiSetup.Repository.RecipeRepository
import com.driven.foodrecipeapp.ApiSetup.Retrofit.RetrofitObject
import com.driven.foodrecipeapp.ApiSetup.ViewModel.RecipeViewModel
import com.driven.foodrecipeapp.NaturalLanguage_Processing.NLP

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Search_By_ingredients.newInstance] factory method to
 * create an instance of this fragment.
 */
class Search_By_ingredients : Fragment() {

    lateinit var binding: FragmentSearchByIngredientsBinding
    lateinit var viewModel: RecipeViewModel
    lateinit var ifactory: ViewModelFactory
    lateinit var iadapter: SIngredientAdapter
    lateinit var irepository: RecipeRepository
    lateinit var nlp: NLP

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchByIngredientsBinding.inflate(layoutInflater, container, false)

        irepository = RecipeRepository(RetrofitObject.api)
        ifactory = ViewModelFactory(irepository)
        viewModel = ViewModelProvider(this, ifactory)[RecipeViewModel::class.java]
        nlp = NLP(requireContext())

        // Initialize adapter here once
        iadapter = SIngredientAdapter(emptyList(), SBIngredients(requireContext()))

        // Set up RecyclerView
        settingRecyclerView()

        // Set up SearchView listener with debouncing
        binding.searchIngredients.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.trim().isNotEmpty()) {
                        binding.searchIngredients.clearFocus()
                        performSearchWithNLP(it)
                    } else {
                        Toast.makeText(requireContext(), "Please Enter Ingredients", Toast.LENGTH_SHORT).show()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.trim().isNotEmpty()) {
                        // Cancel any previously scheduled search
                        searchRunnable?.let { handler.removeCallbacks(it) }

                        // Schedule a new search with a delay (debounce)
                        searchRunnable = Runnable {
                            performSearchWithNLP(it)
                        }
                        handler.postDelayed(searchRunnable!!, 500) // 500ms delay
                    }
                }
                return false
            }
        })

        return binding.root
    }

    private fun performSearchWithNLP(query: String) {
        // Show progress bar and hide other views
        updateUIState(isLoading = true)

        // Process the query through NLP and perform search
        nlp.processNLPQuery(query) { processedQuery ->
            if (processedQuery.isNotEmpty()) {
                searchIngredients(processedQuery)
            } else {
                Toast.makeText(requireContext(), "No valid ingredients found", Toast.LENGTH_SHORT).show()
                updateUIState(isLoading = false)
            }
        }
    }

    private fun settingRecyclerView() {
        // Set up the RecyclerView with a staggered grid layout
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = iadapter

        // Observe data changes and update UI accordingly
        viewModel.ingredientsSearchData.observe(viewLifecycleOwner, Observer { list ->
            updateUIState(isLoading = false)

            if (list.isNotEmpty()) {
                iadapter.updateData(list)
                binding.recyclerView.visibility = View.VISIBLE
                binding.Message.visibility = View.GONE
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.Message.visibility = View.VISIBLE // Show "No Data Available" message
            }
        })
    }

    private fun updateUIState(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar9.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.Message.visibility = View.GONE
        } else {
            binding.progressBar9.visibility = View.GONE
        }
    }

    private fun searchIngredients(ingredients: String) {
        // Perform the search via the ViewModel
        viewModel.fetchRecipesByIngredients(ingredients, 100,"77a8163224fb477e8343482e44d14ebe")
    }

}