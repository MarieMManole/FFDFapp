package com.example.ffbfapp

// WriteReviewActivity.java
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ReviewActivity : AppCompatActivity() {
    private var reviewEditText: EditText? = null
    private var submitReviewButton: Button? = null
    private var reviewsRef: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var restaurantId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_review)
        // Initialize Firebase
        reviewsRef = FirebaseDatabase.getInstance().getReference("reviews")
        firebaseAuth = FirebaseAuth.getInstance()

        // Get restaurant ID from the intent
        restaurantId = intent.getStringExtra("restaurantId")

        // Find views
        reviewEditText = findViewById<EditText>(R.id.editTextReview)
        submitReviewButton = findViewById<Button>(R.id.buttonSubmitReview)

        // Set up click listener for submit review button
        submitReviewButton.setOnClickListener(View.OnClickListener { submitReview() })
    }

    private fun submitReview() {
        val content = reviewEditText!!.text.toString().trim { it <= ' ' }
        if (!content.isEmpty()) {
            val userId: String = firebaseAuth.getCurrentUser().getEmail()
            val review = Review(userId, restaurantId, content, System.currentTimeMillis())
            reviewsRef.push().setValue(review)

            // Show a success message
            Toast.makeText(this, "Review submitted successfully", Toast.LENGTH_SHORT).show()

            // Finish the activity
            finish()
        }
    }
}
