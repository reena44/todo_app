package com.example.reena.utility

import android.text.TextUtils
import java.util.regex.Pattern

public class UserLoginDetail {
    var EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"


    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Pattern.matches(EMAIL_REGEX, email)
    }
    companion object{


        var flag = false
        val sharedPrefFile: String = "pref"
    }

}