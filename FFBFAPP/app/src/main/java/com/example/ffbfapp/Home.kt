package com.example.ffbfapp

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find buttons
        val restaurantsButton = findViewById<Button>(R.id.buttonRestaurants)
        val suggestRestaurantButton = findViewById<Button>(R.id.buttonSuggestRestaurant)
        val forumButton = findViewById<Button>(R.id.buttonForum)
        val profileButton = findViewById<Button>(R.id.buttonProfile)

        // Set up click listeners for each button
        restaurantsButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    RestaurantsActivity::class.java
                )
            )
        }
        suggestRestaurantButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    SuggestRestaurantActivity::class.java
                )
            )
        }
        forumButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    ForumActivity::class.java )
            )
        }
        profileButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    ProfileActivity::class.java)
            )
        }
    }
}