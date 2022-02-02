package com.example.auddistandroid.ui.lecturers.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.R
import com.example.auddistandroid.databinding.FragmentLecturersBinding
import com.example.auddistandroid.ui.login.LoginActivity
import com.example.auddistandroid.utils.ResponseStatus
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

        viewModel.authToken = preferences.getString("authToken", null)!!

        viewModel.lecturers.observe(viewLifecycleOwner) {

            val adapter = LecturersAdapter(it)

            binding.apply {
                if (adapter.itemCount == 0) {
                    textViewLecturersError.text = when (viewModel.responseStatus) {
                        ResponseStatus.NO_RESPONSE -> getString(R.string.server_is_not_responding)
                        ResponseStatus.UNAUTHORIZED -> getString(R.string.unauthorized)
                        ResponseStatus.UNKNOWN_ERROR -> getString(R.string.unknown_error)
                        else -> getString(R.string.list_is_empty)
                    }
                    textViewLecturersError.visibility = View.VISIBLE
                    if (viewModel.responseStatus == ResponseStatus.UNAUTHORIZED) {
                        startActivity(Intent(context, LoginActivity::class.java))
                        requireActivity().finish()
                    }
                } else {
                    recyclerView.layoutManager = LinearLayoutManager(context)
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