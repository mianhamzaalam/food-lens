package com.driven.foodlens.Screens

import SignInService
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.driven.foodlens.R
import com.driven.foodlens.databinding.ActivityEditProfileBinding
import com.driven.foodrecipeapp.User.UserRegistration.Model.User
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        displayUserEmail()

        binding.save.setOnClickListener {
            if (checkValidation()) {
                val name = binding.appCompatEditText.text.toString()
                val password = binding.appCompatEditText2.text.toString()
                val (firstName, lastName) = splitName(name)

                registerUser(firstName, lastName, password)
            }
        }

        val photo = intent.getStringExtra("EditPhoto")
        if (photo!=null){
            Picasso.get().load(photo).into(binding.circleImageView)
        }
        else{
            binding.circleImageView.setImageResource(R.drawable.baseline_person_2_24)
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun displayUserEmail() {
        val email = getSharedPreferences("MyAppPreferences", MODE_PRIVATE).getString("UserMail", "null")
        binding.textView6.text = email
    }

    private fun checkValidation(): Boolean {
        return !binding.appCompatEditText.text.isNullOrEmpty() && !binding.appCompatEditText2.text.isNullOrEmpty()
    }

    private fun splitName(fullName: String): Pair<String, String> {
        val nameParts = fullName.trim().split("\\s+".toRegex())
        val firstName = nameParts.getOrNull(0) ?: ""
        val lastName = nameParts.drop(1).joinToString(" ")
        return Pair(firstName, lastName)
    }

    private fun registerUser(firstName: String, lastName: String, password: String) {
        val email = binding.textView6.text.toString()
        val user = User(firstName, lastName, email, password)

        lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { SignInService(this@EditProfileActivity).signIn(user) }

                result.onSuccess {
                    showToast("Edit Successful")
                    finish() // Navigate back to ProfileFragment
                }.onFailure { exception ->
                    showToast("Edit Failed: ${exception.localizedMessage}")
                }
            } catch (e: Exception) {
                showToast("Error: ${e.localizedMessage}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}