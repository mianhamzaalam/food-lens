package com.driven.foodlens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.driven.foodlens.ProfileFragment.Companion.GOOGLE_PICTURE_KEY
import com.driven.foodlens.ProfileFragment.Companion.GOOGLE_PREFS
import com.driven.foodlens.databinding.FragmentHomeBinding
import com.driven.foodrecipeapp.ActionClass.BookMarkAction.BookMark
import com.driven.foodrecipeapp.ActionClass.NewResultDetail.OnViewDetail
import com.driven.foodrecipeapp.ActionClass.RecommendedDetail.RecommendAction
import com.driven.foodrecipeapp.Adapters.BannerAdapter.ViewPagerAdapter
import com.driven.foodrecipeapp.Adapters.NewAdapter.NewAdapter
import com.driven.foodrecipeapp.Adapters.RecommendedAdapter.RecipeAdapter
import com.driven.foodrecipeapp.Adapters.SeasonAdapter.SeasonAdapter
import com.driven.foodrecipeapp.ApiSetup.Factory.ViewModelFactory
import com.driven.foodrecipeapp.ApiSetup.Model.NewRecipe.NewResult
import com.driven.foodrecipeapp.ApiSetup.Model.Result
import com.driven.foodrecipeapp.ApiSetup.Model.Seasonal.SeasonResult
import com.driven.foodrecipeapp.ApiSetup.Repository.RecipeRepository
import com.driven.foodrecipeapp.ApiSetup.Retrofit.RetrofitObject
import com.driven.foodrecipeapp.ApiSetup.ViewModel.RecipeViewModel
import com.driven.foodrecipeapp.NaturalLanguage_Processing.NLP
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var repository: RecipeRepository
    private lateinit var viewModel: RecipeViewModel
    private var currentPage = 0
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val googlePreferences = requireActivity().getSharedPreferences(GOOGLE_PREFS, MODE_PRIVATE)
        val photo = googlePreferences.getString(GOOGLE_PICTURE_KEY, null)

        if (photo != null) {
            Picasso.get().load(photo).into(binding.profile)
        } else {
            binding.profile.setImageResource(R.drawable.baseline_person_2_24)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        initializeViewModels()
        setupRecyclerViews()
        setupBanner()
        setupSearchBar()

        fetchData()

        binding.profile.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    private fun initializeViewModels() {
        repository = RecipeRepository(RetrofitObject.api)
        viewModel = ViewModelProvider(this, ViewModelFactory(repository))[RecipeViewModel::class.java]
    }

    private fun fetchData() {
        val apiKey = "77a8163224fb477e8343482e44d14ebe"
        viewModel.fetchRecipes(apiKey, "appetizer", 100)
        viewModel.fetchSeasonalRecipes("breakfast", apiKey, 100)
        viewModel.fetchNewRecipes("popular", apiKey, 100)
    }

    private fun setupRecyclerViews() {
        setupRecycler(binding.RecipeRecycler, viewModel.data, ::setupRecommendAdapter, binding.progressBar2, binding.cardView3)
        setupRecycler(binding.weeklyRecipe, viewModel.newRecipeData, ::setupNewAdapter, binding.progressBar3, binding.cardView4)
        setupRecycler(binding.seasonalRecipe, viewModel.seasonalData, ::setupSeasonalAdapter, binding.progressBar4, binding.cardView5)
    }

    private fun <T> setupRecycler(
        recyclerView: RecyclerView,
        liveData: LiveData<List<T>>,
        adapterSetup: (List<T>) -> Unit,
        progressBar: ProgressBar,
        cardView: CardView
    ) {
        liveData.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) {
                adapterSetup(data)
                recyclerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                cardView.visibility = View.GONE
            } else {
                recyclerView.visibility = View.GONE
                progressBar.visibility = View.GONE
                cardView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupRecommendAdapter(data: List<Result>) {
        val adapter = RecipeAdapter(RecommendAction(requireContext()))
        binding.RecipeRecycler.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.RecipeRecycler.adapter = adapter
        adapter.differ.submitList(data)
    }

    private fun setupNewAdapter(data: List<NewResult>) {
        binding.weeklyRecipe.apply {
            layoutManager  = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = NewAdapter(data, BookMark(requireContext()), OnViewDetail(requireContext()))
        }
    }

    private fun setupSeasonalAdapter(data: List<SeasonResult>) {
        binding.seasonalRecipe.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.seasonalRecipe.adapter = SeasonAdapter(data)
    }

    private fun setupBanner() {
        val list = listOf(
            com.driven.foodrecipeapp.Model.Banner(R.drawable.breakfast, "Breakfast"),
            com.driven.foodrecipeapp.Model.Banner(R.drawable.lauch, "Lunch"),
            com.driven.foodrecipeapp.Model.Banner(R.drawable.dinner, "Dinner")
        )
        binding.categories.adapter = ViewPagerAdapter(list)
        binding.categories.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        startAutoScroll()
    }

    private fun startAutoScroll() {
        val runnable = object : Runnable {
            override fun run() {
                currentPage = (currentPage + 1) % binding.categories.adapter!!.itemCount
                binding.categories.currentItem = currentPage
                Handler(Looper.getMainLooper()).postDelayed(this, 3000)
            }
        }
        Handler(Looper.getMainLooper()).post(runnable)
    }

    private fun setupSearchBar() {
        val nlp = NLP(requireContext())

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    // Log current destination for debugging
                    Log.d("Navigation", "Current Destination: ${navController.currentDestination?.label}")

                    val currentDestination = navController.currentDestination
                    if (currentDestination?.id == R.id.homeFragment) {
                        // Ensure navigation is not called multiple times
                        nlp.processNLPQuery(query) { processedQuery ->
                            val bundle = Bundle().apply {
                                putString("SearchQuery", processedQuery)
                            }
                            // Navigate to SearchFragment
                            try {
                                navController.navigate(R.id.action_homeFragment_to_searchFragment, bundle)
                            } catch (e: IllegalArgumentException) {
                                Log.e("NavigationError", "Failed to navigate: ${e.message}")
                            }
                        }
                    } else {
                        Log.e("NavigationError", "Invalid navigation attempt from $currentDestination")
                    }
                } else {
                    Toast.makeText(requireContext(), "Please enter a query", Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
    }
}
