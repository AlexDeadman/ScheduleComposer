package com.example.auddistandroid.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auddistandroid.R
import com.example.auddistandroid.databinding.FragmentListBinding
import com.example.auddistandroid.ui.AbstractViewModel
import com.example.auddistandroid.utils.state.ListState
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

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

        binding.apply {

            val itemAdapter = ItemAdapter<ListItem>()
            initRecyclerView(itemAdapter)

            val viewModel: AbstractViewModel by viewModels()

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is ListState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is ListState.Loaded -> {
                        progressBar.visibility = View.GONE
                        FastAdapterDiffUtil[itemAdapter] = it.result
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
    }

    private fun initRecyclerView(itemAdapter: ItemAdapter<ListItem>) {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)

            val fastAdapter = FastAdapter.with(itemAdapter)
            adapter = fastAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}