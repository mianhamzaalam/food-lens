package com.driven.foodlens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.driven.foodlens.Screens.EditProfileActivity
import com.driven.foodlens.Screens.SignUp
import com.driven.foodlens.databinding.FragmentProfileBinding
import com.google.firebase.FirebaseApp
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
   // private lateinit var googleSign: GoogleSignin

    companion object {
        const val SHARED_PREFS = "MyAppPreferences"
        const val GOOGLE_PREFS = "GoogleService"
        const val GOOGLE_FIRST_NAME_KEY = "GoogleFirstName"
        const val GOOGLE_LAST_NAME_KEY = "GoogleLastName"
        const val GOOGLE_EMAIL_KEY = "GoogleEmail"
        const val GOOGLE_PICTURE_KEY = "GooglePicture"
        const val USER_FIRST_NAME_KEY = "UFirstName"
        const val USER_LAST_NAME_KEY = "ULastName"
        const val USER_EMAIL_KEY = "UserMail"
        const val DEFAULT_NAME = "null"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        FirebaseApp.initializeApp(requireContext())

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Initialize GoogleSign
      //  googleSign = GoogleSignin(requireContext())
       // googleSign.initGoogleService()

        // Get SharedPreferences instances
        val sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val googlePreferences = requireActivity().getSharedPreferences(GOOGLE_PREFS, MODE_PRIVATE)

        // Determine if the user is logged in via Google
        val isGoogleLoggedIn = !googlePreferences.getString(GOOGLE_EMAIL_KEY, null).isNullOrEmpty()

        // Fetch user data based on login type
        val firstName = if (isGoogleLoggedIn) {
            googlePreferences.getString(GOOGLE_FIRST_NAME_KEY, DEFAULT_NAME)
        } else {
            sharedPreferences.getString(USER_FIRST_NAME_KEY, DEFAULT_NAME)
        }

        val lastName = if (isGoogleLoggedIn) {
            googlePreferences.getString(GOOGLE_LAST_NAME_KEY, DEFAULT_NAME)
        } else {
            sharedPreferences.getString(USER_LAST_NAME_KEY, DEFAULT_NAME)
        }

        val email = if (isGoogleLoggedIn) {
            googlePreferences.getString(GOOGLE_EMAIL_KEY, DEFAULT_NAME)
        } else {
            sharedPreferences.getString(USER_EMAIL_KEY, DEFAULT_NAME)
        }

        val photo = googlePreferences.getString(GOOGLE_PICTURE_KEY, null)

        // Set user info in views
        binding.profileNameTextView.text = "$firstName $lastName"
        binding.profileEmailTextView.text = email

        // Load profile image
        if (photo != null) {
            Picasso.get().load(photo).into(binding.profileImageView)
        } else {
            binding.profileImageView.setImageResource(R.drawable.baseline_person_2_24)
        }

        // Edit Profile button click listener
        binding.editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java).apply {
                putExtra("EditFname", firstName)
                putExtra("EditLname", lastName)
                putExtra("EditPicture", photo)
            }
            startActivity(intent)
        }

        // Logout button click listener
        binding.logoutButton.setOnClickListener {
            clearUserSession(sharedPreferences, googlePreferences)
            startActivity(Intent(requireContext(), SignUp::class.java))
            requireActivity().finish()
        }

        return binding.root
    }

    private fun clearUserSession(sharedPreferences: SharedPreferences, googlePreferences: SharedPreferences) {
        // Clear regular user data
        with(sharedPreferences.edit()) {
            clear() // Clears all regular login data
            apply()
        }

        // Clear Google user data
        with(googlePreferences.edit()) {
            clear() // Clears all Google login data
            apply()
        }
    }

}