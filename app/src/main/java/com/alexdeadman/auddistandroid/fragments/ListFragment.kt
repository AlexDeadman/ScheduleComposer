package com.alexdeadman.auddistandroid.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alexdeadman.auddistandroid.R
import com.alexdeadman.auddistandroid.adapters.ListItem
import com.alexdeadman.auddistandroid.databinding.FragmentListBinding
import com.alexdeadman.auddistandroid.service.AudDistApi
import com.alexdeadman.auddistandroid.utils.requireGrandParentFragment
import com.alexdeadman.auddistandroid.utils.state.ListState.*
import com.alexdeadman.auddistandroid.viewmodels.*
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.select.getSelectExtension
import com.mikepenz.fastadapter.utils.ComparableItemListImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var sortingStrategy: Boolean = true

    private val comparator: Comparator<ListItem> =
        Comparator { lhs, rhs ->
            if (sortingStrategy) {
                lhs.entityTitle.compareTo(rhs.entityTitle)
            } else {
                rhs.entityTitle.compareTo(lhs.entityTitle)
            }
        }

    private lateinit var itemAdapter: ItemAdapter<ListItem>
    private lateinit var fastAdapter: FastAdapter<ListItem>

    @Inject
    lateinit var audDistApi: AudDistApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

            val itemListImpl = ComparableItemListImpl(comparator)

            itemAdapter = ItemAdapter(itemListImpl).apply {
                itemFilter.filterPredicate = { item, constraint ->
                    item.entityTitle.lowercase().contains(constraint.toString().lowercase())
                }
            }

            fastAdapter = FastAdapter.with(itemAdapter).apply {
                getSelectExtension().apply { isSelectable = true } // TODO
            }

            recyclerView.apply {
                setHasFixedSize(true)
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
                progressBar.visibility = View.GONE
                swipeRefreshLayout.visibility = View.VISIBLE
                swipeRefreshLayout.isRefreshing = false
                when (state) {
                    is Loaded -> {
                        recyclerView.visibility = View.VISIBLE
                        textViewMassage.visibility = View.GONE
                        FastAdapterDiffUtil[itemAdapter] = state.result.data.map(::ListItem)
                        fastAdapter.withSavedInstanceState(savedInstanceState)
                    }
                    is NoItems -> {
                        recyclerView.visibility = View.GONE
                        textViewMassage.apply {
                            visibility = View.VISIBLE
                            text = getString(R.string.list_is_empty)
                        }
                    }
                    is Error -> {
                        recyclerView.visibility = View.GONE
                        textViewMassage.apply {
                            visibility = View.VISIBLE
                            text = state.message // TODO TEMPO
                        }
                    }
                }
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchEntities()
            }

            imageButtonExpandAll.setOnClickListener {
                // TODO
            }
            imageButtonCollapseAll.setOnClickListener {
                // TODO
            }

            imageButtonSortAsc.setOnClickListener {
                sortingStrategy = true
                itemListImpl.withComparator(comparator)
            }
            imageButtonSortDesc.setOnClickListener {
                sortingStrategy = false
                itemListImpl.withComparator(comparator)
            }

            floatingActionButton.setOnClickListener {
                findNavController().navigate(R.id.createUpdate)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(fastAdapter.saveInstanceState(outState))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchMenuItem = menu.findItem(R.id.action_search)

        (searchMenuItem.actionView as SearchView).setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    itemAdapter.filter(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }
            }
        )
    }
}