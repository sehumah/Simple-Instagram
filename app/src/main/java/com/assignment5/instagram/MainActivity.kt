package com.assignment5.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser


/**
 * Let a user create a post by taking a photo with their camera
 */

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Let the user have a space for:
         *  1. setting the caption of the post
         *  2. a button to launch the camera to take a photo
         *  3. an image view to show the photo the user has taken
         *  4. a button to send and save the post
         */

        // send post to server
        findViewById<Button>(R.id.button_submit).setOnClickListener {
            // grab the caption that the user has inputted
            val caption = findViewById<EditText>(R.id.et_caption).text.toString()
            val user = ParseUser.getCurrentUser()
            submitPost(caption, user)
        }


        // launch camera to let user take a picture
        findViewById<Button>(R.id.button_take_photo).setOnClickListener {
            takePhoto()
        }

        // queryForPosts()
    }


    // submit user's post to server
    private fun submitPost(caption: String, user: ParseUser) {
        // create the post object to send to the server
        val post = Post()
        post.setCaption(caption)
        post.setUser(user)
        post.saveInBackground { exception ->
            if (exception != null) {  // something's gone wrong
                Toast.makeText(this, "Something went wrong. Couldn't save post!", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error while saving post!")
                exception.printStackTrace()
            }
            else {
                Toast.makeText(this, "Successfully saved post!", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Successfully saved post!")
                // todo: reset caption field to be empty again
                // todo: reset the image view to be empty
            }
        }
    }

    // access phone's camera to take photo
    private fun takePhoto() {
        Toast.makeText(this, "Take Photo button clicked!", Toast.LENGTH_SHORT).show()
    }


    // make a query for all posts from the server, todo: replace with something sophisticated
    private fun queryForPosts() {
        // specify the class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // find all post objects in the server
        query.include(Post.KEY_USER)  // return the user object associated with each post
        query.findInBackground(object : FindCallback<Post> {
            override fun done(postObjects: MutableList<Post>?, e: ParseException?) {
                if (e == null) {
                    if (postObjects != null) {
                        for (post in postObjects) {
                            Log.i(TAG, "Post: ${post.getCaption()}, username: ${post.getUser()?.username}")
                        }
                    }
                }
                else {
                    Log.e(TAG, "Error fetching posts!")
                }
            }
        })
    }


    // inflate the resource file to be used and return true to show the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            // navigate to login/signup screen

            // todo: navigating to login activity doesn't seem to be working, figure it out later
            // val intent = Intent(this@MainActivity, LoginActivity::class.java)
            // startActivity(intent)

            // just implement a toast for now
            Toast.makeText(this, "Logout button clicked!", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}
