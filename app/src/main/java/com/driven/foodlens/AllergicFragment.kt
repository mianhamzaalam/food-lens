package com.driven.foodlens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.driven.foodlens.ProfileFragment.Companion.GOOGLE_PREFS
import com.driven.foodlens.ProfileFragment.Companion.SHARED_PREFS
import com.driven.foodlens.Screens.RecipeIngredients
import com.driven.foodlens.databinding.FragmentAllergicBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllergicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllergicFragment : Fragment() {

    private var _binding: FragmentAllergicBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var googlePreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAllergicBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        googlePreferences = requireActivity().getSharedPreferences(GOOGLE_PREFS, MODE_PRIVATE)

        binding.submit.setOnClickListener {
            retrieveData()
        }

        return binding.root
    }

    private fun retrieveData() {
        if (!checkValidation()) {
            Toast.makeText(requireContext(), "Fields required please fill all", Toast.LENGTH_SHORT).show()
            return
        }

        val recipe = binding.editText.text.toString()
        val dietary = binding.editText3.text.toString()
        val allergy = binding.editText2.text.toString()
        val allergyList = allergy.split(",").map { it.trim() }

        submitData(recipe, dietary, allergyList)
    }

    private fun submitData(recipe: String, dietary: String, allergyList: List<String>) {
        binding.loadingView.visibility = View.VISIBLE
        binding.submit.visibility = View.GONE

        val intent = Intent(requireContext(), RecipeIngredients::class.java).apply {
            putExtra("AllergicRecipe", recipe)
            putExtra("AllergicDiet", dietary)
            putStringArrayListExtra("Allergic", ArrayList(allergyList))
        }

        startActivity(intent)

        // Reset visibility after starting the activity
        binding.loadingView.visibility = View.GONE
        binding.submit.visibility = View.VISIBLE
    }

    private fun checkValidation(): Boolean {
        return !(binding.editText.text.isNullOrEmpty() ||
                binding.editText3.text.isNullOrEmpty() ||
                binding.editText2.text.isNullOrEmpty())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
        if (sharedPreferences.getBoolean("ClearUserData", false)) {
            clearUser()
        }
    }

    private fun clearUser(){
        with(sharedPreferences.edit()){
            remove("UserToken")
            remove("UFirstName")
            remove("ULastName")
            apply()
            android.util.Log.d("AllergyFragment","Clearing User")
        }
        with(googlePreferences.edit()){
            remove("GoogleFirstName")
            remove("GoogleLastName")
            remove("GoogleEmail")
            remove("GoogleEmail")
            remove("GooglePicture")
            apply()
            android.util.Log.d("AllergyFragment","Clearing GUser")
        }
    }
}