package com.example.ffbfapp

// ForumActivity.java
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Collections

class ForumActivity : AppCompatActivity() {
    private var postEditText: EditText? = null
    private var postButton: Button? = null
    private var recyclerView: RecyclerView? = null
    private var forumAdapter: ForumAdapter? = null
    private var postList: MutableList<Post>? = null
    private var postsRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum)

        // Initialize views
        postEditText = findViewById<EditText>(R.id.editTextPost)
        postButton = findViewById<Button>(R.id.buttonPost)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewForum)

        // Initialize RecyclerView and adapter
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        postList = ArrayList<Post>()
        forumAdapter = ForumAdapter(postList)
        recyclerView.setAdapter(forumAdapter)

        // Initialize Firebase
        postsRef = FirebaseDatabase.getInstance().getReference("posts")

        // Set up click listener for posting
        postButton.setOnClickListener(View.OnClickListener { post() })

        // Load posts from Firebase
        loadPosts()
    }

    private fun post() {
        val content = postEditText!!.text.toString().trim { it <= ' ' }
        if (!content.isEmpty()) {
            val userId: String = FirebaseAuth.getInstance().getCurrentUser().getEmail()
            val post = Post(userId, content, System.currentTimeMillis())
            postsRef.push().setValue(post)
            postEditText!!.text.clear()
        }
    }

    private fun loadPosts() {
        postsRef.addChildEventListener(object : ChildEventListener() {
            fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val post: Post = dataSnapshot.getValue(Post::class.java)
                if (post != null) {
                    postList!!.add(post)
                    Collections.sort(
                        postList,
                        Collections.reverseOrder<Any>()
                    ) // Ordena de más reciente a más antiguo
                    forumAdapter!!.notifyDataSetChanged()
                }
            } // Other ChildEventListener methods...
        })
    }

    private class ForumAdapter internal constructor(posts: List<Post>?) :
        RecyclerView.Adapter<ForumViewHolder>() {
        private val posts: List<Post>?

        init {
            this.posts = posts
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
            return ForumViewHolder(view)
        }

        override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
            holder.bind(posts!![position])
        }

        override fun getItemCount(): Int {
            return posts!!.size
        }
    }

    private class ForumViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val userTextView: TextView
        private val contentTextView: TextView
        private val timestampTextView: TextView

        init {
            userTextView = itemView.findViewById<TextView>(R.id.textViewUser)
            contentTextView = itemView.findViewById<TextView>(R.id.textViewContent)
            timestampTextView = itemView.findViewById<TextView>(R.id.textViewTimestamp)
        }

        fun bind(post: Post) {
            userTextView.setText(post.getUserId())
            contentTextView.setText(post.getContent())
            // Use a DateFormatter to format the timestamp as a readable date/time
            timestampTextView.text = formatTimestamp(post.getTimestamp())
        }

        private fun formatTimestamp(timestamp: Long): String {
            // Implement your timestamp formatting logic
            return DateFormat.getDateTimeInstance().format(new Date(timestamp));
            return "Formatted Timestamp"
        }
    }
}
