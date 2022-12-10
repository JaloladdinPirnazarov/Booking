package com.klimgroup.booking.DataBase

import android.content.SharedPreferences

class ShPrefManager(private val preferences: SharedPreferences) {
    private val editor = preferences.edit()
    private val usernameKey = "usernameKey"
    private val passwordKey = "passwordKey"
    private val tablesKey = "tablesKey"

    fun saveToken(token: Token){
        editor.apply{
            putString(usernameKey,token.username)
            putString(passwordKey,token.password)
        }
        editor.apply()
    }

    fun getToken():Token{
        val token = Token()
        preferences.apply {
            token.username = getString(usernameKey,"").toString()
            token.password = getString(passwordKey,"").toString()
        }
        return token
    }

    fun deleteToken(){
        editor.apply{
            putString(usernameKey,null)
            putString(passwordKey,null)
        }
        editor.apply()
    }
}