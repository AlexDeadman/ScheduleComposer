package com.example.auddistandroid.ui.lecturers

import android.os.Bundle
import android.util.AndroidRuntimeException
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.auddistandroid.databinding.FragmentLecturersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LecturersFragment : Fragment() {

    private var _binding: FragmentLecturersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLecturersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: LecturersViewModel by viewModels()

        _binding = FragmentLecturersBinding.bind(view)

        viewModel.lecturers.observe(viewLifecycleOwner) {

            val adapter = LecturersAdapter(it)

            binding.apply {
                if (adapter.itemCount == 0) {
                    progressBar.visibility = View.GONE
                    lecturersErrorText.visibility = View.VISIBLE
                } else {
                    recyclerLecturersList.setHasFixedSize(true)
                    recyclerLecturersList.adapter = adapter
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}