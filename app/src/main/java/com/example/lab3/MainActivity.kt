package com.example.lab3

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.contains
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.lab3.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val fragmentManager: FragmentManager = supportFragmentManager

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var title: TextView
    private val kirLatFragment = KirLatFragment()
    private val favouritesFragment = FavouritesFragment()
    private val profileFragment = ProfileFragment()
    private val searchFragment = SearchFragment()
    private val translateFragment = TranslateFragment()
    private var activeFragment: Fragment = KirLatFragment()
    private lateinit var themeModeImage: ImageView
    private var themeMode: Int = 0
    private lateinit var sharedPreferences: SharedPreferences
    private var darkThemeMode=false
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences =
            getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE
            )!!
        themeMode = if(sharedPreferences.contains(getString(R.string.theme_mode))) {
            sharedPreferences.getInt(getString(R.string.theme_mode), DEFAULT_INT_VALUE)
        } else{
            R.style.AppTheme
        }
        super.setTheme(themeMode)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater;

        inflater.inflate(R.menu.action_bar, menu)
        return true
    }

    private fun bindViews() {
        toolbar = findViewById(R.id.toolbar)
        title = findViewById(R.id.name)
        themeModeImage = findViewById(R.id.themeModeImage)
        themeModeImage.setOnClickListener {
            if(sharedPreferences.contains(getString(R.string.boolean_theme))){
                darkThemeMode=sharedPreferences.getBoolean(getString(R.string.boolean_theme),false)
            }
            if(darkThemeMode){
                themeMode = R.style.AppThemeDark
                darkThemeMode=false

            } else{
                themeMode = R.style.AppTheme
                darkThemeMode=true
            }
            savingThemeMode()
            updatingActivity()
        }
        title.text = getString(R.string.kirlat_fragment)
        bottomNavigation = findViewById(R.id.bottomNav)

        setSupportActionBar(toolbar)
        hidingFragments()
    }

    private fun updatingActivity(){
        val intent = intent
        finish()
        startActivity(intent)
    }
    private fun hidingFragments() {
        fragmentManager.beginTransaction().add(R.id.frame, favouritesFragment)
            .hide(favouritesFragment).commit()
        fragmentManager.beginTransaction().add(R.id.frame, profileFragment).hide(profileFragment)
            .commit()
        fragmentManager.beginTransaction().add(R.id.frame, searchFragment).hide(searchFragment)
            .commit()
        fragmentManager.beginTransaction().add(R.id.frame, translateFragment)
            .hide(translateFragment).commit()
        fragmentManager.beginTransaction().add(R.id.frame, kirLatFragment).commit()
        bottomNavigation.selectedItemId = R.id.kirlat
        bottomNavigation.setOnNavigationItemSelectedListener(navListener)
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.kirlat -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(kirLatFragment)
                    .commit()
                activeFragment = kirLatFragment
                title.text = getString(R.string.kirlat_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(profileFragment)
                    .commit()
                activeFragment = profileFragment
                title.text = getString(R.string.profile_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.search -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(searchFragment)
                    .commit()
                activeFragment = searchFragment
                title.text = getString(R.string.search_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.favourite -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(favouritesFragment)
                    .commit()
                activeFragment = favouritesFragment
                title.text = getString(R.string.favourite_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.googleTranslater -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(translateFragment)
                    .commit()
                activeFragment = translateFragment
                title.text = getString(R.string.translate_fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        return@OnNavigationItemSelectedListener false
    }

    private fun savingThemeMode(){
        val editor = sharedPreferences.edit()
        editor?.putInt(getString(R.string.theme_mode), themeMode)
        editor?.putBoolean(getString(R.string.boolean_theme), darkThemeMode)
        editor?.apply()
    }

}
