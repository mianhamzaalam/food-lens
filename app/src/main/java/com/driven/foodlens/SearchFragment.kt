package com.driven.foodlens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.driven.foodlens.databinding.FragmentSearchBinding
import com.driven.foodrecipeapp.ActionClass.SearchDetail.SearchItem
import com.driven.foodrecipeapp.Adapters.SearchAdapter.SearchAdapter
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
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: RecipeViewModel
    private lateinit var cnlp: NLP
    lateinit var sadapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        sadapter = SearchAdapter(SearchItem(requireContext()))

        setupViewModel()
        setupNLP()
        setupRecyclerView()
        setupSearchView()
        retrieveQuery()

        return binding.root
    }

    private fun setupViewModel() {
        val searchRepository = RecipeRepository(RetrofitObject.api)
        val searchFactory = ViewModelFactory(searchRepository)
        viewModel = ViewModelProvider(this, searchFactory).get(RecipeViewModel::class.java) // Corrected ViewModel initialization
    }

    private fun setupNLP() {
        cnlp = NLP(requireContext())
    }

    private fun setupSearchView() {
        binding.querySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.querySearchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.takeIf { it.isNotBlank() }?.let {
                    cnlp.processNLPQuery(it) { processedQuery ->
                        searchQuery(processedQuery)
                    }
                }
                return false
            }
        })
    }

    private fun retrieveQuery() {
        arguments?.getString("SearchQuery")?.let { query ->
            binding.querySearchView.setQuery(query, true)
            searchQuery(query)
        }
    }

    private fun searchQuery(query: String) {
        viewModel.fetchSearchRecipes("77a8163224fb477e8343482e44d14ebe", query, 100) // Adjust to call the correct function
    }

    private fun setupRecyclerView() {
        binding.queryRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.queryRecyclerView.adapter = sadapter

        viewModel.searchData.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                binding.queryRecyclerView.visibility = View.VISIBLE
                binding.progressBar5.visibility = View.GONE
                binding.dataAlert.visibility = View.GONE
                sadapter.differ.submitList(list) // Simplified if using ListAdapter or Differ
            } else {
                binding.queryRecyclerView.visibility = View.GONE
                binding.progressBar5.visibility = View.GONE
                binding.dataAlert.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the search view listener to prevent memory leaks
        binding.querySearchView.setOnQueryTextListener(null)
    }
}