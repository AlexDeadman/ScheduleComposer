package com.alexdeadman.auddistandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.keyIterator
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.alexdeadman.auddistandroid.App.Companion.preferences
import com.alexdeadman.auddistandroid.R
import com.alexdeadman.auddistandroid.databinding.FragmentMainBinding
import com.alexdeadman.auddistandroid.databinding.NavHeaderBinding
import com.alexdeadman.auddistandroid.utils.Keys
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        var currentDestinationId: Int = R.id.schedule
    }

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
            val toolbar = contentMain.appbar.toolbar

            (requireActivity() as AppCompatActivity).apply {
                setSupportActionBar(toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            val childNavController = contentMain.fragmentContainerView
                .getFragment<NavHostFragment>().navController

            childNavController.addOnDestinationChangedListener { _, destination, _ ->
                currentDestinationId = destination.id
                drawerLayout.setDrawerLockMode(
                    if (destination.id == R.id.createUpdate) {
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                    } else {
                        DrawerLayout.LOCK_MODE_UNLOCKED
                    }
                )
            }

            val appBarConfiguration = AppBarConfiguration(
                childNavController.graph.nodes
                    .keyIterator()
                    .asSequence()
                    .filter { it != R.id.createUpdate }
                    .toSet(),
                drawerLayout
            )

            NavigationUI.apply {
                setupActionBarWithNavController(
                    requireActivity() as AppCompatActivity,
                    childNavController,
                    appBarConfiguration
                )
                setupWithNavController(
                    navView,
                    childNavController
                )
            }

            NavHeaderBinding.bind(navView.getHeaderView(0)).textViewSubtitle.text =
                preferences.getString(Keys.USERNAME, "unknown")
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (currentDestinationId != R.id.createUpdate) {
                binding.drawerLayout.open()
            } else {
                requireActivity().onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}