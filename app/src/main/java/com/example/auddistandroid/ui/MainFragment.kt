package com.example.auddistandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.R
import com.example.auddistandroid.databinding.FragmentMainBinding
import com.example.auddistandroid.utils.Keys

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

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
            val appCompatActivity = (requireActivity() as AppCompatActivity).apply {
                setSupportActionBar(contentMain.appbar.toolbar)
            }

            val childNavController = contentMain.mainFragmentContainerView
                .getFragment<NavHostFragment>().navController

            NavigationUI.apply {
                setupActionBarWithNavController(
                    appCompatActivity,
                    childNavController,
                    drawerLayout
                )
                setupWithNavController(
                    navView,
                    childNavController
                )
            }

            navView.apply {
                getHeaderView(0)
                    .findViewById<TextView>(R.id.text_view_nav_header_subtitle)
                    .text = preferences.getString(Keys.USERNAME, "unknown")

                menu.findItem(R.id.drawer_item_settings)?.setOnMenuItemClickListener {
                    findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
                    drawerLayout.close()
                    return@setOnMenuItemClickListener false
                }
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            binding.drawerLayout.open()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}