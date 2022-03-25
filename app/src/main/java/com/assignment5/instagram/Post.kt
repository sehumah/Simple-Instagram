package com.assignment5.instagram

import android.media.Image
import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

/**
 * This class references the Post class created in the Parse server.
 */

@ParseClassName("Post")
class Post : ParseObject() {

    companion object {
        private const val KEY_CAPTION = "caption"
        private const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
    }

    // Every post has a caption, image & a user. create getters & setters for those fields

    /*   Getters   */
    fun getCaption() : String? {
        return getString(KEY_CAPTION)
    }

    fun getImage() : ParseFile? {
        return getParseFile(KEY_IMAGE)
    }

    fun getUser() : ParseUser? {
        return getParseUser(KEY_USER)
    }


    /*   Setters   */
    fun setCaption(caption: String) {
        put(KEY_CAPTION, caption)
    }

    fun setImage(parseFile: ParseFile) {
        put(KEY_IMAGE, parseFile)
    }

    fun setUser(user: ParseUser) {
        put(KEY_USER, user)
    }
}
