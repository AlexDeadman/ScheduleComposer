package com.alexdeadman.schedulecomposer.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun Fragment.requireGrandParentFragment() = requireParentFragment().requireParentFragment()

fun Any?.toStringOrDash() = this?.toString() ?: "—"

inline fun <T> Flow<T>.launchRepeatingCollect(
    lifecycleOwner: LifecycleOwner,
    crossinline action: suspend (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect(action)
        }
    }
}