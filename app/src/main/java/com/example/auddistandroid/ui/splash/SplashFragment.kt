package com.example.auddistandroid.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.auddistandroid.R
import com.example.auddistandroid.ui.MainActivity

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.updateTheme()

        view.postDelayed({
//            val authToken = preferences.getString(Keys.AUTH_TOKEN, null)

//            if (authToken != null) {
//                serviceUpdater.changeAuthToken(authToken)
//                findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
//            } else {
                findNavController().navigate(R.id.action_splashFragment_to_connectionFragment)
//            }
        }, 1000)
    }
}