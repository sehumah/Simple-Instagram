package com.assignment5.instagram

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
         * Testing initial connection

        // create a ParseObject to test the connection
        val parseObject: ParseObject = ParseObject("TestConnectionClass")
        parseObject.put("message", "Test message from Simple-Instagram Android app. Parse is now connected!")
        parseObject.saveInBackground {
            if (it != null) {
                it.localizedMessage?.let { message -> Log.e(TAG, message) }
            }
            else {
                Log.d(TAG, "Object saved!")
            }
        }
        */

        /**
         * Check if there's a current user that is already logged in and prevent the app from taking them to the login page again
         * If there is, take them to MainActivity
         */
        if (ParseUser.getCurrentUser() != null) {
            navigateToMainActivity()
        }

        // create buttons & set their background colors
        val loginButton: Button = findViewById<Button>(R.id.button_login)
        val signupButton: Button = findViewById<Button>(R.id.button_signup)

        loginButton.setBackgroundColor(resources.getColor(R.color.instagram_blue))
        signupButton.setBackgroundColor(Color.GREEN)


        // set click listener to login button & grab the username and password for logging in
        loginButton.setOnClickListener {
            val etUsername = findViewById<EditText>(R.id.et_username).text.toString()
            val etPassword = findViewById<EditText>(R.id.et_password).text.toString()

            // check if username or password field is empty and notify the user
            if (etUsername.isEmpty() && etPassword.isEmpty()) {  // both fields are empty
                Toast.makeText(this, "Enter a username and password!", Toast.LENGTH_SHORT).show()
            }
            else if (etUsername.isEmpty() && etPassword.isNotEmpty()) {  // only username field is empty
                Toast.makeText(this, "Enter a username!", Toast.LENGTH_SHORT).show()
            }
            else if (etPassword.isEmpty() && etUsername.isNotEmpty()) {  // only password field is empty
                Toast.makeText(this, "Enter a password!", Toast.LENGTH_SHORT).show()
            }
            else {
                loginUser(etUsername, etPassword)
            }
        }

        // set click listener to signup button & grab the username and password for signing up
        signupButton.setOnClickListener {
            val etUsername = findViewById<EditText>(R.id.et_username).text.toString()
            val etPassword = findViewById<EditText>(R.id.et_password).text.toString()

            // check if username or password field is empty and notify the user
            if (etUsername.isEmpty() && etPassword.isEmpty()) {  // both fields are empty
                Toast.makeText(this, "Enter a username and password!", Toast.LENGTH_SHORT).show()
            }
            else if (etUsername.isEmpty() && etPassword.isNotEmpty()) {  // only username field is empty
                Toast.makeText(this, "Enter a username!", Toast.LENGTH_SHORT).show()
            }
            else if (etPassword.isEmpty() && etUsername.isNotEmpty()) {  // only password field is empty
                Toast.makeText(this, "Enter a password!", Toast.LENGTH_SHORT).show()
            }
            else {
                signUpUser(etUsername, etPassword)
            }
        }
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
            if (e == null) {  // user has successfully created a new account
                Log.i(TAG, "$username successfully signed up!")
                Toast.makeText(this, "Successfully signed $username up!", Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            }
            else {
                // todo: check if user's account already exists and is trying to sign up again. Tell them to click login instead if they have an account
                // todo: else if another problem, use the below Toast message instead and print the stack trace

                Toast.makeText(this, "Your account exists. Please login!", Toast.LENGTH_SHORT).show()
                e.printStackTrace()  // sign up didn't succeed. look at the parse exception to figure out what happened
            }
        }
    }


    // make network call to log in the user if they have the right info and already have an account
    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {  // user has successfully logged in
                Log.i(TAG, "$username successfully logged in!")
                Toast.makeText(this, "$username successfully logged in!", Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            }
            else {
                // todo: check if user doesn't have an account & tell them to click sign up to create one instead
                // todo: else if another problem, show the below Toast message instead & print the stack trace

                Toast.makeText(this, "Register your account before logging in!", Toast.LENGTH_SHORT).show()
                e.printStackTrace()  // login failed. look at ParseException to see what happened
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
