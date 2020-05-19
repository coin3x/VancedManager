package com.vanced.manager.ui.core

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import com.vanced.manager.R

// This activity will NOT be used in manifest
// since MainActivity will extend it
@SuppressLint("Registered")
open class ThemedActivity : AppCompatActivity() {

    private lateinit var currentTheme: String
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = pref.getString("theme_mode", "LIGHT").toString()

        setFinalTheme(currentTheme)
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            setTaskBG()
        }
    }

    override fun onResume() {
        super.onResume()
        val theme = pref.getString("theme_mode", "LIGHT")

        //if for some weird reasons we get invalid
        //theme, recreate activity
        if (currentTheme != theme)
            recreate()

        //set Task Header color in recents menu for
        //devices that are not using pie recents
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            setTaskBG()
        }
    }

    //This stupid ass AppCompatDelegate does
    //not want to work, so I have to use my
    //own implementation of theme switching

    //This whole theme switcher is a good
    //example of how NOT to write theme switcher.
    //Remember kids, hardcoding is bad
    private fun setFinalTheme(currentTheme: String) {
        val currentAccent = pref.getString("accent_color", "Blue").toString()
        when (currentTheme) {
            "LIGHT" -> setLightAccent(currentAccent)
            "DARK" -> setDarkAccent(currentAccent)
            "FOLLOW" -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> setDarkAccent(currentAccent)
                    Configuration.UI_MODE_NIGHT_NO -> setLightAccent(currentAccent)
                }
            }
            else -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> setDarkAccent(currentAccent)
                    Configuration.UI_MODE_NIGHT_NO -> setLightAccent(currentAccent)
                }
            }
        }
    }

    private fun setDarkAccent(accentColor: String) {
        when (accentColor) {
            "Blue" -> setTheme(R.style.DarkTheme_Blue)
            "Red" -> setTheme(R.style.DarkTheme_Red)
            "Green" -> setTheme(R.style.DarkTheme_Green)
            "Yellow" -> setTheme(R.style.DarkTheme_Yellow)
        }
    }

    private fun setLightAccent(accentColor: String) {
        when (accentColor) {
            "Blue" -> setTheme(R.style.LightTheme_Blue)
            "Red" -> setTheme(R.style.LightTheme_Red)
            "Green" -> setTheme(R.style.LightTheme_Green)
            "Yellow" -> setTheme(R.style.LightTheme_Yellow)
        }
    }

    private fun setTaskBG() {
        val label = getString(R.string.app_name)
        val color = ResourcesCompat.getColor(resources, R.color.Black, null)
        val taskDec: ActivityManager.TaskDescription =
            ActivityManager.TaskDescription(label, null, color)
        setTaskDescription(taskDec)

    }

}