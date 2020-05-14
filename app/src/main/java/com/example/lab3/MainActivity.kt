package com.example.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.lab3.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_kirlat.*

class MainActivity : AppCompatActivity() {
    private val fragmentManager: FragmentManager = supportFragmentManager

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var toolbar: Toolbar

    private val kirLatFragment = KirLatFragment()
    private val favouritesFragment = FavouritesFragment()
    private val profileFragment = ProfileFragment()
    private val searchFragment = SearchFragment()
    private val translateFragment = TranslateFragment()
    private var activeFragment: Fragment = KirLatFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
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
        toolbar.title = getString(R.string.kirlat_fragment)
        bottomNavigation = findViewById(R.id.bottomNav)
        setSupportActionBar(toolbar)
        hidingFragments()
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
        bottomNavigation.setOnNavigationItemSelectedListener(navListener)
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.kirlat -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(kirLatFragment).commit()
                activeFragment = kirLatFragment
                toolbar.title = getString(R.string.kirlat_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(profileFragment).commit()
                activeFragment = profileFragment
                toolbar.title = getString(R.string.profile_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.search -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(searchFragment).commit()
                activeFragment = searchFragment
                toolbar.title = getString(R.string.search_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.favourite -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(favouritesFragment).commit()
                activeFragment = favouritesFragment
                toolbar.title = getString(R.string.favourite_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.googleTranslater -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(translateFragment).commit()
                activeFragment = translateFragment
                toolbar.title = getString(R.string.translate_fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        return@OnNavigationItemSelectedListener false
    }


}
