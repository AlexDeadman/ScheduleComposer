package com.alexdeadman.schedulecomposer.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.textfield.TextInputLayout
import com.validator.textinputvalidator.valid
import com.validator.textinputvalidator.validate
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

fun EditText.accent() {
    requestFocus()
    context?.let {
        val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun TextInputLayout.validate(validators: Collection<(String) -> Pair<Boolean, String>>) {
    validate("", validators) {}
}

fun TextInputLayout.isValid() : Boolean {
    return valid().also {
        if(!it) editText?.accent()
    }
}
