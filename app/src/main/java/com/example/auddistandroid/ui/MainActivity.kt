package com.example.auddistandroid.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.R
import com.example.auddistandroid.databinding.ActivityMainBinding
import com.example.auddistandroid.ui.lecturers.add.AddLecturerActivity
import com.example.auddistandroid.ui.login.LoginActivity
import com.example.auddistandroid.ui.settings.SettingsActivity
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.concurrent.schedule

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var exitToast: Toast
    private lateinit var navController: NavController

    private var backPressedOnce = false

    override fun onBackPressed() {
        if (navController.currentDestination?.id != R.id.navigation_lecturers || backPressedOnce) {
            super.onBackPressed()
        } else {
            backPressedOnce = true
            Timer().schedule(2000) { backPressedOnce = false }
            exitToast.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        preferences.registerOnSharedPreferenceChangeListener(this)

        exitToast = Toast.makeText(
            applicationContext,
            getString(R.string.press_again_to_exit),
            Toast.LENGTH_SHORT
        )

        val navView: NavigationView = binding.navView
        val drawerLayout: DrawerLayout = binding.drawerLayout

        navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_lecturers,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            ),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.apply {
            navView
                .getHeaderView(0)
                .findViewById<TextView>(R.id.text_view_nav_header_subtitle)
                .text = preferences.getString("username", "unknown")

            appBarMain.content.floatingActionButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddLecturerActivity::class.java))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        menu.findItem(R.id.settings_button)
            .setOnMenuItemClickListener {
                startActivity(Intent(this, SettingsActivity::class.java))
                return@setOnMenuItemClickListener true
            }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStop() {
        super.onStop()
        exitToast.cancel()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "authToken") {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        preferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }
}