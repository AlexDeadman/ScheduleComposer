package com.alexdeadman.schedulecomposer.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.adapters.ListItem
import com.alexdeadman.schedulecomposer.databinding.FragmentListBinding
import com.alexdeadman.schedulecomposer.utils.Cringe
import com.alexdeadman.schedulecomposer.utils.InstanceStateKeys
import com.alexdeadman.schedulecomposer.utils.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.utils.requireGrandParentFragment
import com.alexdeadman.schedulecomposer.utils.state.ListState
import com.alexdeadman.schedulecomposer.utils.state.ListState.*
import com.alexdeadman.schedulecomposer.viewmodels.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.MaterialColors
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.select.getSelectExtension
import com.mikepenz.fastadapter.utils.ComparableItemListImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.zip
import javax.inject.Inject
import kotlin.reflect.KClass


@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var itemListImpl: ComparableItemListImpl<ListItem>? = null

    private lateinit var itemAdapter: ItemAdapter<ListItem>
    private lateinit var fastAdapter: FastAdapter<ListItem>

    companion object {
        private const val SORT_ASCENDING = 0
        private const val SORT_DESCENDING = 1
        private const val SORT_NONE = -1
    }

    private var sortingStrategy: Int = SORT_NONE
        set(value) {
            field = value
            itemListImpl?.withComparator(comparator, sortNow = true)
        }

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
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            itemListImpl = ComparableItemListImpl(comparator)

            itemAdapter = ItemAdapter(itemListImpl!!).apply {
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

            val viewModelClasses = when (findNavController().currentDestination!!.id) {
                R.id.classrooms -> ClassroomsViewModel::class to null
                R.id.directions -> DirectionsViewModel::class to null
                R.id.disciplines -> DisciplinesViewModel::class to SyllabusesViewModel::class
                R.id.groups -> GroupsViewModel::class to SyllabusesViewModel::class
                R.id.lecturers -> LecturersViewModel::class to DisciplinesViewModel::class
                R.id.syllabuses -> SyllabusesViewModel::class to DirectionsViewModel::class
                else -> throw IllegalStateException()
            }

            val mainViewModel = getViewModel(viewModelClasses.first)
            val relatedViewModel = viewModelClasses.second?.let { getViewModel(it) }

            mainViewModel.state
                .filterNotNull()
                .apply {
                    @Cringe
                    if (relatedViewModel != null) {
                        zip(relatedViewModel.state.filterNotNull(), ::Pair)
                            .launchRepeatingCollect(viewLifecycleOwner) {
                                handleStates(it.first, it.second, savedInstanceState)
                            }
                    } else {
                        launchRepeatingCollect(viewLifecycleOwner) {
                            handleStates(it, null, savedInstanceState)
                        }
                    }
                }

            swipeRefreshLayout.apply {
                setOnRefreshListener {
                    mainViewModel.fetchEntities()
                    relatedViewModel?.fetchEntities()
                }
                setColorSchemeResources(android.R.color.white)
                setProgressBackgroundColorSchemeColor(
                    MaterialColors.getColor(view, R.attr.colorPrimary)
                )
            }

            imageButtonExpandAll.setOnClickListener { toggleAll(expand = true) }
            imageButtonCollapseAll.setOnClickListener { toggleAll(expand = false) }

            imageButtonSortAsc.setOnClickListener { sortingStrategy = SORT_ASCENDING }
            imageButtonSortDesc.setOnClickListener { sortingStrategy = SORT_DESCENDING }

            val bottomSheetDialog = BottomSheetDialog(requireContext()).apply {
                setContentView(R.layout.fragment_create_update)
                behavior.isHideable = false
            }

            floatingActionButton.setOnClickListener { bottomSheetDialog.show() }
        }
    }

    private fun getViewModel(clazz: KClass<out AbstractViewModel>): AbstractViewModel =
        ViewModelProvider(
            requireGrandParentFragment(),
            viewModelFactory.withClass(clazz)
        )[clazz.java]

    private fun handleStates(
        mainState: ListState,
        relatedState: ListState?,
        savedInstanceState: Bundle?,
    ) {
        binding.apply {
            progressBar.visibility = View.GONE
            swipeRefreshLayout.apply {
                visibility = View.VISIBLE
                isRefreshing = false
            }

            when (mainState) {
                is Loaded -> {
                    textViewMassage.visibility = View.GONE
                    floatingActionButton.visibility = View.VISIBLE
                    FastAdapterDiffUtil[itemAdapter] = mainState.result.data.map {
                        @Cringe // TODO                         TEMPO
                        if (relatedState != null && relatedState is Loaded) {
                            ListItem(it, relatedState.result.data)
                        } else {
                            ListItem(it)
                        }
                    }
                    fastAdapter.withSavedInstanceState(savedInstanceState)
                }
                is NoItems -> {
                    itemAdapter.clear()
                    textViewMassage.apply {
                        visibility = View.VISIBLE
                        text = resources.getString(R.string.list_is_empty)
                    }
                    floatingActionButton.visibility = View.VISIBLE
                }
                is Error -> {
                    itemAdapter.clear() // TODO mb snackbar
                    textViewMassage.apply {
                        visibility = View.VISIBLE
                        text = resources.getString(mainState.messageStringId)
                    }
                    floatingActionButton.visibility = View.GONE
                }
            }
        }
    }

    private fun toggleAll(expand: Boolean) {
        itemAdapter.adapterItems.forEach { it.expanded = expand }
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