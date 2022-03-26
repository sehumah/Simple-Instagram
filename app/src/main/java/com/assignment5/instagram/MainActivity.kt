package com.assignment5.instagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
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
import com.parse.*
import java.io.File


/**
 * Let a user create a post by taking a photo with their camera
 */

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
        private val photoFileName = "photo.jpg"
        private var photoFile: File? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // fill username textview with user's username when they login
        findViewById<TextView>(R.id.tv_post_owner_username).text = ParseUser.getCurrentUser().username

        // send post to server
        findViewById<Button>(R.id.button_submit).setOnClickListener {
            // grab the caption that the user has inputted
            val caption = findViewById<EditText>(R.id.et_caption).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile == null && caption == "") {  // check if both fields are null
                Toast.makeText(this, "Post is empty!", Toast.LENGTH_SHORT).show()
            }
            else if (photoFile == null && caption != "") {  // check if photo field is null
                Toast.makeText(this, "No image found. Please add a photo!", Toast.LENGTH_SHORT).show()
            }
            else if (photoFile != null && caption == "") { // check if caption field is null
                Toast.makeText(this, "No caption found. Please add a caption!", Toast.LENGTH_SHORT).show()
            }
            else if (photoFile != null && caption != "") {  // both fields are filled, submit the post
                submitPost(caption, user, photoFile!!)
            }
            else {  // some other error
                Toast.makeText(this, "Error sending post. Please try again later!", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error. Could not send post at this moment!")
            }
        }


        // launch camera to let user take a picture
        findViewById<Button>(R.id.button_launch_camera).setOnClickListener {
            onLaunchCamera()
        }

        // queryForPosts()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                val ivPreview: ImageView = findViewById(R.id.iv_image_post)
                ivPreview.setImageBitmap(takenImage)
            }
            else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri = FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }


    // Returns the File for a photo stored on disk given the fileName
    private fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }


    // submit user's post to server
    private fun submitPost(caption: String, user: ParseUser, photoFile: File) {
        // create the post object to send to the server
        val post = Post()
        post.setImage(ParseFile(photoFile))
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

                // display the progress bar while post is being submitted
                val pbSubmittingPost = findViewById<ProgressBar>(R.id.pb_submitting_post)
                pbSubmittingPost.visibility = ProgressBar.VISIBLE

                // launch new mainactivity intent & close the old one for now, until discovering an even better solution
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                // todo: reset caption field to be empty again
                // todo: reset the image view to be empty
            }
        }
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

    // handle clicks on an app bar menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {

            // log the current user out if they click the logout button
            ParseUser.logOut()
            val currentUser: ParseUser = ParseUser.getCurrentUser()  // this will now be null

            // todo: navigating to login activity doesn't seem to be working so far, figure it out later
            // then navigate to login/signup screen
            // val intent = Intent(this@MainActivity, LoginActivity::class.java)
            // startActivity(intent)

            // just implement a toast for now
            // Toast.makeText(this, "Logout button clicked!", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}
