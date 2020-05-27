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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.lab3.fragment.*
import com.example.lab3.utils.DEFAULT_INT_VALUE
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val fragmentManager: FragmentManager = supportFragmentManager

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var title: TextView
    private val kirLatFragment = KirLatFragment()
    private val favouritesFragment = FavouritesFragment()
    private val profileFragment = ProfileFragment()
    private val searchFragment = InterestsFragment()
    private val translateFragment = TranslateFragment()
    private var activeFragment: Fragment = KirLatFragment()
    private lateinit var themeModeImage: ImageView
    private var themeMode: Int = 0
    private lateinit var sharedPreferences: SharedPreferences
    private var darkThemeMode=false
    private var isTranslateFragment = false
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences =
            getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE
            )!!
        themeMode = if(sharedPreferences.contains(getString(R.string.theme_mode))) {
            sharedPreferences.getInt(getString(R.string.theme_mode),
                DEFAULT_INT_VALUE
            )
        } else{
            R.style.AppTheme
        }
        super.setTheme(themeMode)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
    }


    private fun bindViews() {
        toolbar = findViewById(R.id.toolbar)
        title = findViewById(R.id.name)
        themeModeImage = findViewById(R.id.themeModeImage)
        toolbar.title=""
        toolbar.popupTheme=R.style.ToolbarTheme
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
        fragmentManager.beginTransaction().add(R.id.frame, kirLatFragment).commit()
        bottomNavigation.selectedItemId = R.id.kirlat
        bottomNavigation.setOnNavigationItemSelectedListener(navListener)
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.kirlat -> {
                activeFragment = kirLatFragment
                fragmentManager.beginTransaction().replace(R.id.frame, activeFragment)
                    .commit()
                title.text = getString(R.string.kirlat_fragment)
                isTranslateFragment=false
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                activeFragment = profileFragment
                fragmentManager.beginTransaction().replace(R.id.frame, activeFragment)
                    .commit()
                title.text = getString(R.string.profile_fragment)
                isTranslateFragment=false
                return@OnNavigationItemSelectedListener true
            }
            R.id.search -> {
                activeFragment = searchFragment
                fragmentManager.beginTransaction().replace(R.id.frame, activeFragment)
                    .commit()
                title.text = getString(R.string.search_fragment)
                isTranslateFragment=false
                return@OnNavigationItemSelectedListener true
            }
            R.id.favourite -> {
                activeFragment = favouritesFragment
                fragmentManager.beginTransaction().replace(R.id.frame, activeFragment)
                    .commit()
                title.text = getString(R.string.favourite_fragment)
                isTranslateFragment=false
                return@OnNavigationItemSelectedListener true
            }
            R.id.googleTranslater -> {
                activeFragment = translateFragment
                fragmentManager.beginTransaction().replace(R.id.frame, activeFragment)
                    .commit()
                title.text = getString(R.string.translate_fragment)
                isTranslateFragment=true
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
