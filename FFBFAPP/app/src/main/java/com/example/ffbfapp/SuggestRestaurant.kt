package com.example.ffbfapp

// SuggestRestaurantActivity.java
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class SuggestRestaurantActivity : AppCompatActivity() {
    private var nameEditText: EditText? = null
    private var locationEditText: EditText? = null
    private var restaurantImageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggest_restaurant)

        // Find views
        nameEditText = findViewById<EditText>(R.id.editTextRestaurantName)
        locationEditText = findViewById<EditText>(R.id.editTextLocation)
        restaurantImageView = findViewById<ImageView>(R.id.imageViewRestaurant)
        val selectImageButton = findViewById<Button>(R.id.buttonSelectImage)
        val submitButton = findViewById<Button>(R.id.buttonSubmitSuggestion)

        // Set up click listeners
        selectImageButton.setOnClickListener { openImagePicker() }
        submitButton.setOnClickListener { submitSuggestion() }
    }

    private fun openImagePicker() {
        // Implement image picker logic (e.g., using Intent.ACTION_PICK)
        // This is a simplified example, you may need to add permission checks
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun submitSuggestion() {
        // Implement logic to submit suggestion to Firebase
        // You may need to validate the input fields and handle the image upload
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI and load it into the ImageView
            val imageUri = data.data
            Glide.with(this).load(imageUri).into(restaurantImageView)
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
