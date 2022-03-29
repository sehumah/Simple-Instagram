package com.assignment5.instagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.assignment5.instagram.fragments.ComposeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File


/**
 * Let a user create a post by taking a photo with their camera
 */


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // define fragment manager
        val fragmentManager: FragmentManager = supportFragmentManager


        // set click listeners to the bottom navigation bar items
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view).setOnItemSelectedListener { item ->
            lateinit var currentFragment: Fragment  // temporary fragment to use for swapping actual fragments
            when (item.itemId) {
                R.id.action_home -> {
                    currentFragment = ComposeFragment() // TODO("navigate to the home/timeline screen later when it's been implemented")
                }
                R.id.action_search -> {
                    currentFragment = ComposeFragment() // TODO("navigate to the search/explore screen later when it's been implemented")
                }
                R.id.action_reels -> {
                    currentFragment = ComposeFragment() // TODO("navigate to the reels screen later when it's been implemented")
                }
                R.id.action_shopping_cart -> {
                    currentFragment = ComposeFragment() // TODO("navigate to the shopping cart screen later when it's been implemented")
                }
                R.id.action_profile -> {
                    currentFragment = ComposeFragment() // TODO("navigate to the profile screen later when it's been implemented")
                }
                else -> {
                    currentFragment = ComposeFragment() // TODO("navigate to the timeline screen later when it's been implemented")
                }
            }
            fragmentManager.beginTransaction().replace(R.id.fl_fragment_container, currentFragment).commit()
            // return true to say that we've handled this user interaction on the item
            true
        }

        // set default selection
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view).selectedItemId = R.id.fl_fragment_container  // todo: change destination later to home when home fragment is implemented

         // queryForPosts()
    }


    // inflate the resource file to be used and return true to show the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    // handle clicks on an app bar menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {

            // log the current user out if they click the logout button
            ParseUser.logOut()
            val currentUser: ParseUser = ParseUser.getCurrentUser()  // this will now be null

            /*
            // todo: navigating to login activity after logging out doesn't seem to be working so far, figure it out later
             then navigate to login/signup screen
             val intent = Intent(this@MainActivity, LoginActivity::class.java)
             startActivity(intent)

             // just implement a toast for now
             Toast.makeText(this, "Logout button clicked!", Toast.LENGTH_SHORT).show()
            */
        }
        else if (item.itemId == R.id.action_compose) {  // listen for clicks on the compose button in the top menu
            // show the compose fragment
            val fragment = ComposeFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fl_fragment_container, fragment)
            fragmentTransaction.commit()
        }
        return super.onOptionsItemSelected(item)
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

}


/**
 * Suggestions for improvement:
 *  1. currently, the main activity screen is relaunched upon submitting a post but what should
 *      actually happen is setting the imagefield and the caption field to empty instead.
 *
 *  2. the app crushes after logging the second user out, reach out to TAs and seek help to prevent
 *      this from happening later on
 */
