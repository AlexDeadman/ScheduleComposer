package com.alexdeadman.auddistandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexdeadman.auddistandroid.R
import com.alexdeadman.auddistandroid.adapters.ListItem
import com.alexdeadman.auddistandroid.databinding.FragmentListBinding
import com.alexdeadman.auddistandroid.service.AudDistApi
import com.alexdeadman.auddistandroid.utils.state.ListState
import com.alexdeadman.auddistandroid.viewmodels.AbstractViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.expandable.getExpandableExtension
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment() {

    @Inject
    lateinit var audDistApi: AudDistApi

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

            val itemAdapter = ItemAdapter<ListItem>().apply {
                fastAdapter?.getExpandableExtension()
            }

            binding.recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = FastAdapter.with(itemAdapter)
            }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}