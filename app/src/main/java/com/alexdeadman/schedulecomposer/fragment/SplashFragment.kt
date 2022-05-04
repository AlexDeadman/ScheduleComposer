package com.alexdeadman.schedulecomposer.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.MainActivity
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.util.key.PreferenceKeys

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.updateTheme()

        view.postDelayed({
            findNavController().navigate(
                if (preferences.getString(PreferenceKeys.AUTH_TOKEN, null) != null) {
                    R.id.action_splashFragment_to_mainFragment
                } else {
                    R.id.action_splashFragment_to_connectionFragment
                }
            )
        }, 1000)
    }
}