package com.alexdeadman.auddistandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexdeadman.auddistandroid.R
import com.alexdeadman.auddistandroid.adapters.ListItem
import com.alexdeadman.auddistandroid.adapters.ListSubItem
import com.alexdeadman.auddistandroid.databinding.FragmentListBinding
import com.alexdeadman.auddistandroid.service.AudDistApi
import com.alexdeadman.auddistandroid.utils.requireGrandParentFragment
import com.alexdeadman.auddistandroid.utils.state.ListState.*
import com.alexdeadman.auddistandroid.viewmodels.*
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.expandable.getExpandableExtension
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var audDistApi: AudDistApi

    private lateinit var fastAdapter: FastAdapter<ListItem>

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
                itemFilter.filterPredicate = { item, constraint ->
                    item.title.lowercase().contains(constraint.toString().lowercase())
                }
            }

            materialEditTextSearch.addTextChangedListener { itemAdapter.filter(it) }

            fastAdapter = FastAdapter.with(itemAdapter).apply {
                getExpandableExtension()
//                getSelectExtension().apply { isSelectable = true }
            }

            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = fastAdapter
            }

            val viewModelClass = when (MainFragment.currentDestinationId) {
                R.id.audiences -> AudiencesViewModel::class
                R.id.directions -> DirectionsViewModel::class
                R.id.disciplines -> DisciplinesViewModel::class
                R.id.groups -> GroupsViewModel::class
                R.id.lecturers -> LecturersViewModel::class
                R.id.syllabuses -> SyllabusesViewModel::class
                else -> throw IllegalStateException()
            }

            val viewModel = ViewModelProvider(
                requireGrandParentFragment(),
                ViewModelFactory(viewModelClass, audDistApi)
            ).get(viewModelClass.java)

            viewModel.state.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is Loaded -> {
                        progressBar.visibility = View.GONE
                        swipeRefreshLayout.isRefreshing = false
                        FastAdapterDiffUtil[itemAdapter] = state.result.data.map {
                            ListItem(it).withSubItem(ListSubItem(it))
                        }
                        fastAdapter.withSavedInstanceState(savedInstanceState)
                    }
                    is NoItems -> {
                        progressBar.visibility = View.GONE
                        textViewError.apply {
                            visibility = View.VISIBLE
                            text = getString(R.string.list_is_empty)
                        }
                    }
                    is Error -> {
                        progressBar.visibility = View.GONE
                        textViewError.apply {
                            visibility = View.VISIBLE
                            text = getString(R.string.unknown_error)
                        }
                        // TODO TEMPO
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchEntities()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(
            fastAdapter.saveInstanceState(outState)
        )
    }
}