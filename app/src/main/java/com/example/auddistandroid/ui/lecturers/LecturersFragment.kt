package com.example.auddistandroid.ui.lecturers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auddistandroid.R
import com.example.auddistandroid.data.model.DataList
import com.example.auddistandroid.databinding.FragmentListBinding
import com.example.auddistandroid.ui.CustomAdapter
import com.example.auddistandroid.ui.CustomViewModel
import com.example.auddistandroid.utils.state.ListState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LecturersFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: CustomViewModel by viewModels()

        viewModel.state.observe(viewLifecycleOwner) {

            binding.apply {

                when (it) {
                    is ListState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is ListState.Loaded<*> -> {
                        progressBar.visibility = View.GONE

                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = CustomAdapter(it.data as DataList<*>)
                        }
                    }
                    is ListState.NoItems -> {
                        progressBar.visibility = View.GONE

                        textViewError.apply {
                            visibility = View.VISIBLE
                            text = getString(R.string.list_is_empty)
                        }
                    }
                    is ListState.Error -> {
                        progressBar.visibility = View.GONE

                        textViewError.apply {
                            visibility = View.VISIBLE
                            text = getString(R.string.unknown_error)
                        }

                        // TODO TEMPO
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.fetchEntities()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}