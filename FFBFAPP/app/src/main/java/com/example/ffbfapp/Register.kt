package com.example.ffbfapp

// RegisterActivity.java
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class RegisterActivity : AppCompatActivity() {
    private var firstNameEditText: EditText? = null
    private var lastNameEditText: EditText? = null
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var registerButton: Button? = null
    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Find views
        firstNameEditText = findViewById<EditText>(R.id.editTextFirstName)
        lastNameEditText = findViewById<EditText>(R.id.editTextLastName)
        emailEditText = findViewById<EditText>(R.id.editTextEmail)
        passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        registerButton = findViewById<Button>(R.id.buttonRegister)

        // Set up register button click listener
        registerButton.setOnClickListener(View.OnClickListener { registerUser() })
    }

    private fun registerUser() {
        val firstName = firstNameEditText!!.text.toString().trim { it <= ' ' }
        val lastName = lastNameEditText!!.text.toString().trim { it <= ' ' }
        val email = emailEditText!!.text.toString().trim { it <= ' ' }
        val password = passwordEditText!!.text.toString().trim { it <= ' ' }

        // Check if fields are not empty
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)
            || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
        ) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Register user with email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?>() {
                fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful()) {
                        // Registration successful
                        // You can save additional user details in the database here
                        Toast.makeText(
                            this@RegisterActivity, "Registration successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigate to the login activity or main activity
                        finish() // close the registration activity
                    } else {
                        // If registration fails, display a message to the user.
                        if (task.getException() is FirebaseAuthUserCollisionException) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Email is already registered. Please log in.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registration failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
    }
}
