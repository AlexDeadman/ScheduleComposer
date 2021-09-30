package com.example.auddistandroid.ui.lecturers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.auddistandroid.R
import com.example.auddistandroid.databinding.FragmentLecturersBinding
import com.example.auddistandroid.ui.QueryStatus
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
                    errorTextView.text = when (viewModel.queryStatus) {
                        QueryStatus.NO_INTERNET -> getString(R.string.no_internet_connection)
                        QueryStatus.NO_RESPONSE -> getString(R.string.server_is_not_responding)
                        QueryStatus.UNAUTHORIZED -> getString(R.string.unauthorized)
                        QueryStatus.UNKNOWN_ERROR -> getString(R.string.unknown_error)
                        else -> getString(R.string.list_is_empty)
                    }
                    errorTextView.visibility = View.VISIBLE
                } else {
                    recyclerView.setHasFixedSize(true)
                    recyclerView.adapter = adapter
                }
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}