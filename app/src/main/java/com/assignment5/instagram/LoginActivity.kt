package com.assignment5.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseObject
import com.parse.ParseUser


/**
 * Contains the UI and logic to let the user login to the Parse server
 */

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /**
         * Check if there's a current user that is already logged in | and prevent the app from taking them to the login page again
         * If there is, take them to MainActivity
         */
        if (ParseUser.getCurrentUser() != null) {
            navigateToMainActivity()
        }

        // set click listener to login button & grab the username and password for logging in
        findViewById<Button>(R.id.button_login).setOnClickListener {
            val etUsername = findViewById<EditText>(R.id.et_username).text.toString()
            val etPassword = findViewById<EditText>(R.id.et_password).text.toString()
            loginUser(etUsername, etPassword)
        }

        // set click listener to signup button & grab the username and password for signing up
        findViewById<Button>(R.id.button_signup).setOnClickListener {
            val etUsername = findViewById<EditText>(R.id.et_username).text.toString()
            val etPassword = findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(etUsername, etPassword)
        }

        /**
         * Testing initial connection

        // create a ParseObject to test the connection
        val parseObject: ParseObject = ParseObject("TestConnectionClass")
        parseObject.put("message", "Test message from Simple-Instagram Android app. Parse is now connected!")
        parseObject.saveInBackground {
            if (it != null) {
                it.localizedMessage?.let { message -> Log.e(TAG, message) }
            } else {
                Log.d(TAG, "Object saved!")
            }
        }
        */
    }


    // make network call to sign up a user with their username and password if they don't have an account
    private fun signUpUser(username: String, password: String){
        // create a new ParseUser object for the user
        val newUser: ParseUser = ParseUser()

        // set the fields for the user to be created
        newUser.username = username
        newUser.setPassword(password)

        // sign the user up in the background
        newUser.signUpInBackground { e ->
            if (e == null) {
                // user has successfully created a new account
                // TODO: show a successful sign up Toast message
                // TODO: navigate the user to main activity
            }
            else {
                // todo: show an unsuccessful sign up toast
                e.printStackTrace()  // sign up didn't succeed. look at the parse exception to figure out what happened
            }
        }
    }


    // make network call to log in the user if they have the right info and already have an account
    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {  // user has successfully logged in
                Log.i(TAG, "$username successfully logged in!")
                Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            }
            else {
                e.printStackTrace()  // login failed. look at ParseException to see what happened
                Toast.makeText(this, "Unable to log in. Try again later!", Toast.LENGTH_SHORT).show()
            }
            })
        )
    }


    // take the user to MainActivity once they successfully log in
    private fun navigateToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()  // to prevent user from going back to login page after successfully logging in
    }
}
