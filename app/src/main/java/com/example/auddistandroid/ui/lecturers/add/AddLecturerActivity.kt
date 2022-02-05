package com.example.auddistandroid.ui.lecturers.add

import android.os.Bundle
import com.example.auddistandroid.databinding.ActivityAddLecturerBinding
import com.example.auddistandroid.ui.base.BaseActivity

class AddLecturerActivity : BaseActivity() {
    private lateinit var binding: ActivityAddLecturerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddLecturerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}