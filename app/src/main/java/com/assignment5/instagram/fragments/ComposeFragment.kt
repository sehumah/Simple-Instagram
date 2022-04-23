package com.assignment5.instagram.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.assignment5.instagram.MainActivity
import com.assignment5.instagram.Post
import com.assignment5.instagram.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File
import java.sql.Time
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ComposeFragment : Fragment() {

    private val TAG = "ComposeFragment"
    private val photoFileName = "photo.jpg"
    private var photoFile: File? = null
    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    lateinit var ivImagePost: ImageView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // fill post owner's username textview with their username
        container?.findViewById<TextView>(R.id.tv_post_owner_username)?.text = ParseUser.getCurrentUser().username

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }


    // set onclicklisteners and setup logic
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get reference to the image to post view
        ivImagePost = view.findViewById(R.id.iv_image_post)

        // set click listener to submit button to send post to server
        view.findViewById<Button>(R.id.button_submit).setOnClickListener {  // since fragments can't automatically findViewById, prepend it with the view variable
            // grab the caption that the user has inputted
            val caption = view.findViewById<EditText>(R.id.et_caption).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile == null && caption == "") {  // check if both fields are null
                Toast.makeText(requireContext(), "Post is empty!", Toast.LENGTH_SHORT).show()
            }
            else if (photoFile == null && caption != "") {  // check if photo field is null
                Toast.makeText(requireContext(), "No image found. Please add a photo!", Toast.LENGTH_SHORT).show()
            }
            else if (photoFile != null && caption == "") { // check if caption field is null
                Toast.makeText(requireContext(), "No caption found. Please add a caption!", Toast.LENGTH_SHORT).show()
            }
            else if (photoFile != null && caption != "") {  // both fields are filled, submit the post
                submitPost(caption, user, photoFile!!)

                // set caption & image fields to be empty
                view.findViewById<EditText>(R.id.et_caption).setText("")
                ivImagePost.setImageResource(0)
            }
            else {  // some other error
                Toast.makeText(requireContext(), "Error sending post. Please try again later!", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error. Could not send post at this moment!")
            }
        }


        // launch camera to let user take a photo
        view.findViewById<Button>(R.id.button_launch_camera).setOnClickListener {
            onLaunchCamera()
        }
    }


    // submit user's post to server
    private fun submitPost(caption: String, user: ParseUser, photoFile: File) {

        // create the post object to send to the server
        val post = Post()
        post.setImage(ParseFile(photoFile))
        post.setCaption(caption)
        post.setUser(user)

        // save the post to the server
        post.saveInBackground { exception ->
            if (exception != null) {  // something's gone wrong
                Toast.makeText(requireContext(), "Something went wrong. Couldn't save post!", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error while saving post!")
                exception.printStackTrace()
            }
            else {
                Toast.makeText(requireContext(), "Successfully saved post!", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Successfully saved post!")

                // display the progress bar while post is being submitted
                val pbSubmittingPost = view?.findViewById<ProgressBar>(R.id.pb_submitting_post)
                pbSubmittingPost?.visibility = ProgressBar.VISIBLE

                // launch new main activity intent & close the old one for now, until a better solution is discovered
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                activity?.finish()

                // todo: reset caption field to be empty again
                // todo: reset the image view to be empty
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
            val fileProvider: Uri = FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
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
        val mediaStorageDir = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivImagePost.setImageBitmap(takenImage)
            }
            else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
