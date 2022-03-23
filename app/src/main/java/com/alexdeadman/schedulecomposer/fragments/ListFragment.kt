package com.alexdeadman.schedulecomposer.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.adapters.ListItem
import com.alexdeadman.schedulecomposer.databinding.FragmentListBinding
import com.alexdeadman.schedulecomposer.utils.InstanceStateKeys
import com.alexdeadman.schedulecomposer.utils.launchRepeatedCollect
import com.alexdeadman.schedulecomposer.utils.requireGrandParentFragment
import com.alexdeadman.schedulecomposer.utils.state.ListState.Loaded
import com.alexdeadman.schedulecomposer.utils.state.ListState.NoItems
import com.alexdeadman.schedulecomposer.viewmodels.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.select.getSelectExtension
import com.mikepenz.fastadapter.utils.ComparableItemListImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val SORT_ASCENDING = 0
        private const val SORT_DESCENDING = 1
        private const val SORT_NONE = -1
    }
    private lateinit var itemAdapter: ItemAdapter<ListItem>
    private lateinit var fastAdapter: FastAdapter<ListItem>

    private var sortingStrategy: Int = SORT_NONE

    private val comparator: Comparator<ListItem> =
        Comparator { lhs, rhs ->
            when (sortingStrategy) {
                SORT_ASCENDING -> lhs.entityTitle.compareTo(rhs.entityTitle)
                SORT_DESCENDING -> rhs.entityTitle.compareTo(lhs.entityTitle)
                SORT_NONE -> 0
                else -> throw IllegalStateException()
            }
        }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var searchView: SearchView
    private var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.apply {
            searchQuery = getString(InstanceStateKeys.SEARCH_QUERY)
            sortingStrategy = getInt(InstanceStateKeys.SORTING_STRATEGY)
        }
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
                viewModelFactory.withClass(viewModelClass)
            )[viewModelClass.java]

            viewModel.state
                .filterNotNull()
                .launchRepeatedCollect(viewLifecycleOwner) { state ->
                    progressBar.visibility = View.GONE
                    swipeRefreshLayout.apply {
                        visibility = View.VISIBLE
                        isRefreshing = false
                    }
                    when (state) {
                        is Loaded -> {
                            textViewMassage.visibility = View.GONE
                            FastAdapterDiffUtil[itemAdapter] = state.result.data.map(::ListItem)
                            fastAdapter.withSavedInstanceState(savedInstanceState)
                        }
                        is NoItems -> {
                            itemAdapter.clear()
                            textViewMassage.apply {
                                visibility = View.VISIBLE
                                text = resources.getString(state.messageStringId)
                            }
                        }
                    }
                }

            swipeRefreshLayout.setOnRefreshListener { viewModel.fetchEntities() }

            imageButtonExpandAll.setOnClickListener { /*TODO*/ }
            imageButtonCollapseAll.setOnClickListener { /*TODO*/ }

            imageButtonSortAsc.setOnClickListener {
                sortingStrategy = SORT_ASCENDING
                itemListImpl.withComparator(comparator, sortNow = true)
            }
            imageButtonSortDesc.setOnClickListener {
                sortingStrategy = SORT_DESCENDING
                itemListImpl.withComparator(comparator, sortNow = true)
            }

            val bottomSheetDialog = BottomSheetDialog(requireContext()).apply {
                setContentView(R.layout.fragment_create_update)
                behavior.isHideable = false
            }

            floatingActionButton.setOnClickListener { bottomSheetDialog.show() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchMenuItem = menu.findItem(R.id.action_search)
        searchView = searchMenuItem.actionView as SearchView

        searchView.setOnQueryTextListener(
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

        if (!searchQuery.isNullOrBlank()) {
            searchMenuItem.expandActionView()
            searchView.apply {
                setQuery(searchQuery, false)
                clearFocus()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putString(InstanceStateKeys.SEARCH_QUERY, searchView.query.toString())
            putInt(InstanceStateKeys.SORTING_STRATEGY, sortingStrategy)
        }
        super.onSaveInstanceState(fastAdapter.saveInstanceState(outState))
    }
}