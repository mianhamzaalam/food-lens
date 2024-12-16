package com.driven.foodlens.Screens
import ModelTextAdapter
import RecipeAdapter
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.driven.foodlens.databinding.ActivityAiDetailBinding
import com.driven.foodrecipeapp.AiModel_Handling.ImagePrediction.Model.ApiResponse
import com.driven.foodrecipeapp.AiModel_Handling.ImagePrediction.Object.ImagePredictObject
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class AiDetail : AppCompatActivity() {

    private lateinit var binding: ActivityAiDetailBinding
    private lateinit var sharedPreferences: SharedPreferences

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdgeLayout()
        setupRecyclerViews()

        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)

        val dataImageUri = intent.getStringExtra("ImageSet")
        val token = getUserToken()

        dataImageUri?.let { uri ->
            loadImage(uri)
            uploadImageToServer(token, Uri.parse(uri))
        }
    }

    private fun setupEdgeToEdgeLayout() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun setupRecyclerViews() {
        binding.aiIngredients.layoutManager = LinearLayoutManager(this)
        binding.textViews.layoutManager = LinearLayoutManager(this)
    }

    private fun getUserToken(): String? {
        return sharedPreferences.getString("UserToken", null) ?: getGoogleToken()
    }

    private fun getGoogleToken(): String? {
        val googlePrefs = getSharedPreferences("GoogleService", MODE_PRIVATE)
        return googlePrefs.getString("GoogleToken", null)
    }

    private fun loadImage(uri: String) {
        Picasso.get().load(uri).into(binding.imageView)
    }

    private fun uploadImageToServer(token: String?, imageUri: Uri) {
        coroutineScope.launch {
            try {
                val file = getFileFromUri(imageUri) ?: throw Exception("File not found")
                val convertedFile = if (!isValidImageFile(file)) convertImageToJpgOrPng(file) else file
                val mediaType = getMediaType(convertedFile.extension)
                val requestFile = RequestBody.create(mediaType.toMediaTypeOrNull(), convertedFile)
                val body = MultipartBody.Part.createFormData("file", convertedFile.name, requestFile)

                binding.progressBar8.visibility = View.VISIBLE
                val response = withContext(Dispatchers.IO) {
                    ImagePredictObject.api.predictImage(token ?: "", body)
                }
                handleResponse(response)
            } catch (e: Exception) {
                binding.progressBar8.visibility = View.GONE
                showToast("Error: ${e.localizedMessage}")
                Log.e("AiDetail", "Error uploading image", e)
            }
        }
    }

    private fun isValidImageFile(file: File): Boolean {
        return when (file.extension.lowercase()) {
            "jpg", "jpeg", "png" -> true
            else -> false
        }
    }

    private fun getMediaType(extension: String): String {
        return when (extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            else -> "application/octet-stream"
        }
    }

    private fun handleResponse(response: Response<ApiResponse>) {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val recipe = response.body()?.Response?.recipes ?: emptyList()
                    if (recipe.isEmpty()) {
                        binding.dataAlert.visibility = View.VISIBLE
                        binding.progressBar8.visibility = View.GONE
                        return@withContext
                    }

                    val name = recipe.map { it.recipe_name }
                    val ingredients = recipe.flatMap { it.ingredients }

                    Log.d("RecipeName", name.toString())
                    Log.d("Ingredients", ingredients.toString())

                    binding.textViews.adapter = ModelTextAdapter(recipe)
                    binding.aiIngredients.adapter = RecipeAdapter(recipe)

                    binding.textViews.visibility = View.VISIBLE
                    binding.aiIngredients.visibility = View.VISIBLE
                    binding.progressBar8.visibility = View.GONE
                } else {
                    binding.dataAlert.visibility = View.VISIBLE
                    binding.progressBar8.visibility = View.GONE
                    showToast("Error: ${response.errorBody()?.string()}")
                }
            }
        }
    }

    private fun convertImageToJpgOrPng(file: File): File {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
        val convertedFile = File(cacheDir, "converted_image.jpg")

        FileOutputStream(convertedFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        return convertedFile
    }

    private fun getFileFromUri(uri: Uri): File? {
        return when (uri.scheme) {
            "content" -> contentResolver.openInputStream(uri)?.use { inputStream ->
                val fileName = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    cursor.getString(nameIndex)
                } ?: "temp_image"

                val tempFile = File(cacheDir, fileName)
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                tempFile
            }
            "file" -> File(uri.path ?: return null)
            else -> null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // Cancel coroutines when activity is destroyed
    }
}
