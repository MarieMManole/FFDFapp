package com.example.ffbfapp

// ProfileActivity.java
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class ProfileActivity : AppCompatActivity() {
    private var emailTextView: TextView? = null
    private var firstNameEditText: EditText? = null
    private var lastNameEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var saveChangesButton: Button? = null
    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Find views
        emailTextView = findViewById<TextView>(R.id.textViewEmail)
        firstNameEditText = findViewById<EditText>(R.id.editTextFirstName)
        lastNameEditText = findViewById<EditText>(R.id.editTextLastName)
        passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        saveChangesButton = findViewById<Button>(R.id.buttonSaveChanges)

        // Set initial user data
        val currentUser: FirebaseUser = firebaseAuth.getCurrentUser()
        if (currentUser != null) {
            emailTextView.setText(currentUser.getEmail())
            firstNameEditText.setText(currentUser.getDisplayName())
        }

        // Set up click listener for save changes button
        saveChangesButton.setOnClickListener(View.OnClickListener { saveChanges() })
    }

    private fun saveChanges() {
        val currentUser: FirebaseUser = firebaseAuth.getCurrentUser()
        if (currentUser != null) {
            // Get updated user input
            val newFirstName = firstNameEditText!!.text.toString().trim { it <= ' ' }
            val newLastName = lastNameEditText!!.text.toString().trim { it <= ' ' }
            val newPassword = passwordEditText!!.text.toString().trim { it <= ' ' }

            // Validate input
            if (TextUtils.isEmpty(newFirstName)) {
                Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show()
                return
            }

            // Update user display name
            val profileUpdates: UserProfileChangeRequest = Builder()
                .setDisplayName(newFirstName)
                .build()
            currentUser.updateProfile(profileUpdates)

            // Update password if provided
            if (!TextUtils.isEmpty(newPassword)) {
                currentUser.updatePassword(newPassword)
            }

            // Show a success message
            Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_SHORT).show()
        }
    }
}
