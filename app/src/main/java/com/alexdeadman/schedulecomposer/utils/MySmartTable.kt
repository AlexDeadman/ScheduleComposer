package com.alexdeadman.schedulecomposer.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.bin.david.form.core.SmartTable
import kotlin.reflect.full.memberFunctions

class MySmartTable<T> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SmartTable<T>(context, attrs) {

    override fun onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow()
        } catch (e: Exception) {
            try {
                SmartTable::class.memberFunctions
                    .single { it.name == "release" }
                    .call(this)
            } catch (ex: Exception) {
                Log.e(
                    this::class.simpleName,
                    "onDetachedFromWindow: ${ex.stackTrace}"
                )
            }
        }
    }
}