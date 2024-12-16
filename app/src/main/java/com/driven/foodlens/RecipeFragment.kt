package com.driven.foodlens

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.driven.foodlens.ProfileFragment.Companion.GOOGLE_PREFS
import com.driven.foodlens.ProfileFragment.Companion.SHARED_PREFS
import com.driven.foodlens.databinding.FragmentRecipeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var googlePreferences: SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        googlePreferences = requireActivity().getSharedPreferences(GOOGLE_PREFS, MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.apply {
            searchByIngredients.setOnClickListener {
                navController.navigate(R.id.action_recipeFragment_to_search_By_ingredients)
            }
            allergy.setOnClickListener {
                navController.navigate(R.id.action_recipeFragment_to_allergicFragment)
            }
            yourRecipe.setOnClickListener {
                navController.navigate(R.id.action_recipeFragment_to_bookMarkFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding to avoid memory leaks
    }
    private fun clearUser() {
        with(sharedPreferences.edit()) {
            remove("UserToken")
            remove("UFirstName")
            remove("ULastName")
            apply()
            android.util.Log.d("RecipeFragment", "Clearing User")
        }
        with(googlePreferences.edit()) {
            remove("GoogleFirstName")
            remove("GoogleLastName")
            remove("GoogleEmail")
            remove("GoogleEmail")
            remove("GooglePicture")
            apply()
            android.util.Log.d("RecipeFragment", "Clearing GUser")
        }
    }
}