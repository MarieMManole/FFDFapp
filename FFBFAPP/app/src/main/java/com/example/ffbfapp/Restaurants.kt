package com.example.ffbfapp

// RestaurantsActivity.java
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RestaurantsActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var restaurantAdapter: RestaurantAdapter? = null
    private var restaurantList: MutableList<Restaurant>? = null
    private var restaurantsRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)

        // Initialize RecyclerView and adapter
        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewRestaurants)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        restaurantList = ArrayList<Restaurant>()
        restaurantAdapter = RestaurantAdapter(restaurantList)
        recyclerView.setAdapter(restaurantAdapter)

        // Initialize Firebase
        restaurantsRef = FirebaseDatabase.getInstance().getReference("restaurants")
        // Load restaurants from Firebase
        loadRestaurants()
    }
    private fun loadRestaurants() {
        restaurantsRef.addValueEventListener(object : ValueEventListener() {
            fun onDataChange(dataSnapshot: DataSnapshot) {
                restaurantList!!.clear()
                for (restaurantSnapshot in dataSnapshot.getChildren()) {
                    val restaurant: Restaurant = restaurantSnapshot.getValue(Restaurant::class.java)
                    if (restaurant != null) {
                        restaurantList!!.add(restaurant)
                    }
                }
                restaurantAdapter!!.notifyDataSetChanged()
            }

            fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
    private class RestaurantAdapter internal constructor(restaurants: List<Restaurant>?) :
        RecyclerView.Adapter<RestaurantViewHolder>() {
        private val restaurants: List<Restaurant>?

        init {
            this.restaurants = restaurants
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
            return RestaurantViewHolder(view)
        }

        override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
            holder.bind(restaurants!![position])
        }

        override fun getItemCount(): Int {
            return restaurants!!.size
        }
    }

    private class RestaurantViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView
        private val nameTextView: TextView
        private val descriptionTextView: TextView

        init {
            imageView = itemView.findViewById<ImageView>(R.id.imageViewRestaurant)
            nameTextView = itemView.findViewById<TextView>(R.id.textViewRestaurantName)
            descriptionTextView =
                itemView.findViewById<TextView>(R.id.textViewRestaurantDescription)
        }

        fun bind(restaurant: Restaurant) {
            // Load image using Glide or your preferred image loading library
            Glide.with(itemView.context)
                .load(restaurant.getImageUrl())
                .into(imageView)
            nameTextView.setText(restaurant.getName())
            descriptionTextView.setText(restaurant.getDescription())
        }
    }
}

