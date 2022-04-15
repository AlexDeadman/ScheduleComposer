package com.alexdeadman.schedulecomposer.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.adapters.ListItem
import com.alexdeadman.schedulecomposer.databinding.FragmentListBinding
import com.alexdeadman.schedulecomposer.databinding.ListItemBinding
import com.alexdeadman.schedulecomposer.dialog.ConfirmDialog
import com.alexdeadman.schedulecomposer.dialog.LecturerDialog
import com.alexdeadman.schedulecomposer.utils.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.utils.provideViewModel
import com.alexdeadman.schedulecomposer.utils.state.ListState
import com.alexdeadman.schedulecomposer.utils.state.ListState.*
import com.alexdeadman.schedulecomposer.viewmodels.*
import com.google.android.material.color.MaterialColors
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.BindingViewHolder
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.utils.ComparableItemListImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.zip
import javax.inject.Inject


@AndroidEntryPoint
class ListFragment : Fragment(), ConfirmDialog.ConfirmationListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemListImpl: ComparableItemListImpl<ListItem>
    private lateinit var itemAdapter: ItemAdapter<ListItem>
    private lateinit var fastAdapter: FastAdapter<ListItem>
    private lateinit var mainViewModel: AbstractViewModel
    private lateinit var searchView: SearchView
    private lateinit var createUpdateDialog: LecturerDialog // TODO TEMPO

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val confirmDialog = ConfirmDialog()

    // TODO REFACTOR
    private val editClickEventHook = object : ClickEventHook<ListItem>() {
        override fun onBind(viewHolder: RecyclerView.ViewHolder): View =
            ((viewHolder as BindingViewHolder<*>).binding as ListItemBinding).imageButtonEdit

        override fun onClick(
            v: View,
            position: Int,
            fastAdapter: FastAdapter<ListItem>,
            item: ListItem,
        ) {
            mainViewModel.currentEntity = item.entity
            createUpdateDialog.show(childFragmentManager, null)
        }
    }
    private val deleteClickEventHook = object : ClickEventHook<ListItem>() {
        override fun onBind(viewHolder: RecyclerView.ViewHolder): View =
            ((viewHolder as BindingViewHolder<*>).binding as ListItemBinding).imageButtonDelete

        override fun onClick(
            v: View,
            position: Int,
            fastAdapter: FastAdapter<ListItem>,
            item: ListItem,
        ) {
            mainViewModel.currentEntity = item.entity
            confirmDialog.show(childFragmentManager, null)
        }
    }
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            itemListImpl = ComparableItemListImpl({ _, _ -> 0 })

            itemAdapter = ItemAdapter(itemListImpl).apply {
                itemFilter.filterPredicate = { item, constraint ->
                    // FIXME case trouble
                    item.entityTitle.lowercase().contains(constraint.toString().lowercase())
                }
            }

            fastAdapter = FastAdapter.with(itemAdapter).apply {
//                getSelectExtension().apply { isSelectable = true } // TODO
                addEventHooks(listOf(editClickEventHook, deleteClickEventHook))
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

            mainViewModel = provideViewModel(viewModelFactory, viewModelClasses.first)
            val relatedViewModel = viewModelClasses.second?.let {
                provideViewModel(viewModelFactory, it)
            }

            mainViewModel.comparator?.let { itemListImpl.withComparator(it, true) }

            mainViewModel.fetchState
                .filterNotNull()
                .apply {
                    if (relatedViewModel != null) {
                        zip(relatedViewModel.fetchState.filterNotNull(), ::Pair)
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

            imageButtonSortAsc.setOnClickListener {
                updateComparator { left, right -> left.entityTitle.compareTo(right.entityTitle) }
            }
            imageButtonSortDesc.setOnClickListener {
                updateComparator { left, right -> right.entityTitle.compareTo(left.entityTitle) }
            }

            createUpdateDialog = LecturerDialog()

            floatingActionButton.setOnClickListener {
                mainViewModel.currentEntity = null
                createUpdateDialog.show(childFragmentManager, null)
            }
        }
    }

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
                isEnabled = true
            }
            when (mainState) {
                is Loaded -> {
                    textViewMassage.visibility = View.GONE
                    floatingActionButton.visibility = View.VISIBLE
                    FastAdapterDiffUtil[itemAdapter] = mainState.result.data.map {
                        if (relatedState != null) {
                            if (relatedState is Loaded) {
                                ListItem(it, relatedState.result.data)
                            } else {
                                // FIXME
                                mainViewModel.fetchState.value =
                                    Error(R.string.unknown_error /*TODO TEMPO*/)
                                return
                            }
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
                        text = getString(R.string.list_is_empty)
                    }
                    floatingActionButton.visibility = View.VISIBLE
                }
                is Error -> {
                    itemAdapter.clear() // TODO mb snackbar
                    textViewMassage.apply {
                        visibility = View.VISIBLE
                        text = getString(mainState.messageStringId)
                    }
                    floatingActionButton.visibility = View.GONE
                }
            }
        }
    }

    private fun toggleAll(expand: Boolean) {
        itemAdapter.adapterItems.forEach { it.expanded = expand }
    }

    private fun updateComparator(rule: (ListItem, ListItem) -> Int) {
        Comparator<ListItem>(rule).let {
            itemListImpl.withComparator(it, true)
            mainViewModel.comparator = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchMenuItem = menu.findItem(R.id.action_search)

        searchView = (searchMenuItem.actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    itemAdapter.filter(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean = false
            })
        }

        mainViewModel.searchQuery?.let {
            if (it.isNotBlank()) {
                searchMenuItem.expandActionView()
                searchView.apply {
                    setQuery(it, false)
                    clearFocus()
                }
            }
        }
    }

    override fun confirmButtonClicked() {
        mainViewModel.run {
            deleteEntity(currentEntity!!.id)
            // TODO
//            fetchState.value = Loaded((fetchState.value as Loaded).result.also { it.data.remove(currentEntity) })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mainViewModel.searchQuery = searchView.query.toString()
        super.onSaveInstanceState(fastAdapter.saveInstanceState(outState))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}