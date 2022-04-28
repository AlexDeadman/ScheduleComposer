package com.alexdeadman.schedulecomposer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.alexdeadman.schedulecomposer.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var topLevelDestinations: Set<Int>
    private lateinit var childNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            (requireActivity() as AppCompatActivity).apply {
                setSupportActionBar(contentMain.appbar.toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            childNavController = contentMain.fragmentContainerView
                .getFragment<NavHostFragment>().navController

            topLevelDestinations = navView.menu
                .iterator()
                .asSequence()
                .map { it.itemId }
                .toSet()

            NavigationUI.apply {
                setupActionBarWithNavController(
                    requireActivity() as AppCompatActivity,
                    childNavController,
                    AppBarConfiguration(
                        topLevelDestinations,
                        drawerLayout
                    )
                )
                setupWithNavController(
                    navView,
                    childNavController
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (childNavController.currentDestination!!.id in topLevelDestinations) {
                binding.drawerLayout.open()
            } else {
                requireActivity().onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}