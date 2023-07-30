package com.mymuslem.sarrawi

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class  SharedPref(var con: Context) {
    var isDark: Boolean= false

//    fun saveThemeStatePref(isDark: Boolean) {
//        val pref = con.getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)
//
//        val editor = pref.edit()
//        editor.putBoolean("is_dark_mode", isDark)
//        editor.commit()
//    }
//
//    fun getThemeStatePref(): Boolean {
//        val pref =
//            con.getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)
//
//        return pref.getBoolean("is_dark_mode", false)
//    }
    private val sharedPreferences =
    con.getSharedPreferences("MyPrefss", AppCompatActivity.MODE_PRIVATE)

    fun saveThemeStatePref(isDark: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_dark_mode", isDark)
        editor.apply()
    }

    fun getThemeStatePref(): Boolean {
        return sharedPreferences.getBoolean("is_dark_mode", false)
    }
}