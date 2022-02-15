package com.example.auddistandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.R
import com.example.auddistandroid.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

class MainFragment : Fragment() {

//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var exitToast: Toast
//    private lateinit var navController: NavController

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var drawer: DrawerLayout

//    private var backPressedOnce = false

//    override fun onBackPressed() {
//        if (navController.currentDestination?.id != R.id.navigation_lecturers || backPressedOnce) {
//            super.onBackPressed()
//        } else {
//            backPressedOnce = true
//            Timer().schedule(2000) { backPressedOnce = false }
//            exitToast.show()
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            drawer = drawerLayout

            val navController = contentMain.mainFragmentContainerView
                .getFragment<NavHostFragment>().navController
            val appCompatActivity = (activity as AppCompatActivity)

            appCompatActivity.apply {
                setSupportActionBar(binding.contentMain.toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            NavigationUI.apply {
                setupActionBarWithNavController(
                    appCompatActivity,
                    navController,
                    drawerLayout
                )
                setupWithNavController(
                    navView,
                    navController
                )
            }

            navView.getHeaderView(0)
                .findViewById<TextView>(R.id.text_view_nav_header_subtitle)
                .text = preferences.getString("username", "unknown")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawer.open()
        }
        return super.onOptionsItemSelected(item)
    }

    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//        preferences.registerOnSharedPreferenceChangeListener(this)
//
//        exitToast = Toast.makeText(
//            applicationContext,
//            getString(R.string.press_again_to_exit),
//            Toast.LENGTH_SHORT
//        )
//
//        val navView: NavigationView = binding.navView
//        val drawerLayout: DrawerLayout = binding.drawerLayout
//
//        navController = findNavController(R.id.nav_host_fragment_content_main)
//
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_lecturers,
//                R.id.navigation_dashboard,
//                R.id.navigation_notifications
//            ),
//            drawerLayout
//        )
//
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//

//    }
//
//
//
//    override fun onStop() {
//        super.onStop()
//        exitToast.cancel()
//    }
//
//    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
//        if (key == "authToken") {
//            startActivity(Intent(this, LoginFragment::class.java))
//            finish()
//        }
//    }
//
//    override fun onDestroy() {
//        preferences.unregisterOnSharedPreferenceChangeListener(this)
//        super.onDestroy()
//    }
}