package com.alexdeadman.schedulecomposer.dialog

import android.content.Context
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class AbstractBottomSheetDialog<T : ViewBinding>(context: Context) :
    BottomSheetDialog(context) {

    @Suppress("PropertyName")
    protected var _binding: T? = null
    protected val binding get() = _binding!!

    abstract fun createBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createBinding()
        setContentView(binding.root)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isDraggable = false
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}