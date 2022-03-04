package com.alexdeadman.auddistandroid.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexdeadman.auddistandroid.App.Companion.preferences
import com.alexdeadman.auddistandroid.MainActivity
import com.alexdeadman.auddistandroid.R
import com.alexdeadman.auddistandroid.utils.Keys

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.updateTheme()

        view.postDelayed({
            findNavController().navigate(
                if (preferences.getString(Keys.AUTH_TOKEN, null) != null) {
                    R.id.action_splashFragment_to_mainFragment
                } else {
                    R.id.action_splashFragment_to_connectionFragment
                }
            )
        }, 1000)
    }
}