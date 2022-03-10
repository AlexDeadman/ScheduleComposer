package com.alexdeadman.auddistandroid.utils

import androidx.fragment.app.Fragment

fun Fragment.requireGrandParentFragment() = requireParentFragment().requireParentFragment()

fun Any?.toStringOrDash() = this?.toString() ?: "—"