package com.driven.foodlens

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.driven.foodlens.MainActivity.Companion.CLEAR_USER_DATA_FLAG
import com.driven.foodlens.ProfileFragment.Companion.GOOGLE_PREFS
import com.driven.foodlens.ProfileFragment.Companion.SHARED_PREFS
import com.driven.foodlens.databinding.FragmentBookMarkBinding
import com.driven.foodrecipeapp.Adapters.BookMarkAdapter.BookMarkAdapter
import com.driven.foodrecipeapp.BookMark_Implementation.BookMarkDataBase.BookMarkDatabase
import com.driven.foodrecipeapp.BookMark_Implementation.BookMarkFactory.BookFactory
import com.driven.foodrecipeapp.BookMark_Implementation.BookMarkRepository.BookMark_Repository
import com.driven.foodrecipeapp.BookMark_Implementation.BookMarkViewModel.BookMark_ViewModel
import com.driven.foodrecipeapp.BookMark_Implementation.DAO.BookMarkDao
import com.driven.foodrecipeapp.BookMark_Implementation.Entity.BookMark_Recipe

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookMarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookMarkFragment : Fragment() {

    private var _binding: FragmentBookMarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookAdapter: BookMarkAdapter
    private lateinit var bookDao: BookMarkDao
    private lateinit var bookRepository: BookMark_Repository
    private lateinit var bookFactory: BookFactory
    private lateinit var bookViewModel: BookMark_ViewModel

    private lateinit var googlePreferences: SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookMarkBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        googlePreferences = requireActivity().getSharedPreferences(GOOGLE_PREFS, MODE_PRIVATE)

        setupViewModel()
        setupRecyclerView()

        return binding.root
    }

    private fun setupViewModel() {
        bookDao = BookMarkDatabase.getDataBase(requireContext()).dao()
        bookRepository = BookMark_Repository(bookDao)
        bookFactory = BookFactory(bookRepository)
        bookViewModel = ViewModelProvider(this, bookFactory)[BookMark_ViewModel::class.java]

        bookViewModel.savedRecipes.observe(viewLifecycleOwner) { list ->
            Log.d("BookCheck", list.toString())
            updateUI(list)
        }
    }

    private fun updateUI(list: List<BookMark_Recipe>) {
        if (list.isNotEmpty()) {
            binding.collection.visibility = View.VISIBLE
            binding.bookmarkNotification.visibility = View.GONE
            bookAdapter.differ.submitList(list)
        } else {
            binding.collection.visibility = View.GONE
            binding.bookmarkNotification.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        bookAdapter = BookMarkAdapter(requireContext()) { itemCount ->
            // Update UI based on item count
            if (itemCount == 0) {
                binding.collection.visibility = View.GONE
                binding.bookmarkNotification.visibility = View.VISIBLE
            } else {
                binding.collection.visibility = View.VISIBLE
                binding.bookmarkNotification.visibility = View.GONE
            }
        }
        binding.collection.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.collection.adapter = bookAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding to avoid memory leaks
        if (sharedPreferences.getBoolean(CLEAR_USER_DATA_FLAG, false)) {
            clearUser()
        }
    }

    private fun clearUser() {
        with(sharedPreferences.edit()) {
            remove("UserToken")
            remove("UFirstName")
            remove("ULastName")
            apply()
            Log.d("BookMarkFragment", "Clearing User")
        }
        with(googlePreferences.edit()) {
            remove("GoogleFirstName")
            remove("GoogleLastName")
            remove("GoogleEmail")
            remove("GoogleEmail")
            remove("GooglePicture")
            apply()
            Log.d("BookMarkFragment", "Clearing GUser")
        }
    }
}