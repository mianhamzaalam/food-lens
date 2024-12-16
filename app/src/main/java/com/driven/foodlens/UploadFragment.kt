package com.driven.foodlens

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import com.driven.foodlens.MainActivity.Companion.CLEAR_USER_DATA_FLAG
import com.driven.foodlens.Screens.AiDetail
import com.driven.foodlens.databinding.FragmentUploadBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UploadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadFragment : Fragment() {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    private val CAMERA_REQUEST_CODE = 1001
    private val GALLERY_REQUEST_CODE = 1002

    private var imageUri: Uri? = null
    private lateinit var photoFile: File

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var googlePreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyAppPreferences", Activity.MODE_PRIVATE)
        googlePreferences = requireContext().getSharedPreferences("GoogleService", Activity.MODE_PRIVATE)

        binding.uploadImageButton.setOnClickListener { openGallery() }
        binding.takePhotoButton.setOnClickListener { openCamera() }
        binding.submitButton.setOnClickListener { submitImage() }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun openCamera() {
        photoFile = createImageFile()
        imageUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", photoFile)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun submitImage() {
        imageUri?.let {
            val intent = Intent(requireContext(), AiDetail::class.java).apply {
                putExtra("ImageSet", it.toString())
            }
            startActivity(intent)
        } ?: showToast("Please select an image first")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        imageUri = uri
                        showToast("Image selected from gallery")
                    }
                }
                CAMERA_REQUEST_CODE -> {
                    showToast(if (imageUri != null) "Photo captured successfully" else "Photo capture failed")
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (sharedPreferences.getBoolean(CLEAR_USER_DATA_FLAG, false)) {
            clearUser()
        }
    }

    private fun clearUser(){
        with(sharedPreferences.edit()) {
            remove("UserToken")
            remove("UFirstName")
            remove("ULastName")
            putBoolean(CLEAR_USER_DATA_FLAG, false)  // Reset the flag
            apply()
            Log.d("Upload", "Clearing User")
        }
        with(googlePreferences.edit()) {
            remove("GoogleFirstName")
            remove("GoogleLastName")
            remove("GoogleEmail")
            remove("GooglePicture")
            putBoolean(CLEAR_USER_DATA_FLAG, false)  // Reset the flag
            apply()
            Log.d("Upload", "Clearing GUser")
        }
    }
}