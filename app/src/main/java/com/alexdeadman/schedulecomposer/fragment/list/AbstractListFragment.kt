package com.alexdeadman.schedulecomposer.fragment.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.adapter.ListItem
import com.alexdeadman.schedulecomposer.databinding.FragmentListBinding
import com.alexdeadman.schedulecomposer.databinding.ListItemBinding
import com.alexdeadman.schedulecomposer.dialog.ConfirmationDialog
import com.alexdeadman.schedulecomposer.dialog.addedit.AbstractAddEditDialog
import com.alexdeadman.schedulecomposer.util.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.util.provideViewModel
import com.alexdeadman.schedulecomposer.util.show
import com.alexdeadman.schedulecomposer.util.state.ListState
import com.alexdeadman.schedulecomposer.util.state.SendingState
import com.alexdeadman.schedulecomposer.viewmodel.AbstractEntityViewModel
import com.alexdeadman.schedulecomposer.viewmodel.ViewModelFactory
import com.google.android.material.color.MaterialColors
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.select.getSelectExtension
import com.mikepenz.fastadapter.utils.ComparableItemListImpl
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import kotlin.reflect.KClass


abstract class AbstractListFragment : Fragment(), ConfirmationDialog.ConfirmationListener {

    protected abstract val mainViewModelClass: KClass<out AbstractEntityViewModel>
    protected abstract val relatedViewModelClass: KClass<out AbstractEntityViewModel>?
    protected abstract val addEditDialogClass: KClass<out AbstractAddEditDialog<out ViewBinding>>

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override var confirmationMessage: String? = null
    private val confirmationDialog = ConfirmationDialog()

    private lateinit var itemListImpl: ComparableItemListImpl<ListItem>
    private lateinit var itemAdapter: ItemAdapter<ListItem>
    private lateinit var fastAdapter: FastAdapter<ListItem>
    private lateinit var searchView: SearchView

    private lateinit var mainViewModel: AbstractEntityViewModel
    private var relatedViewModel: AbstractEntityViewModel? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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
                    item.entity.title.contains(constraint!!, true)
                }
            }

            fastAdapter = FastAdapter.with(itemAdapter).apply {
                getSelectExtension().apply { isSelectable = true }
                addClickListener({ binding: ListItemBinding ->
                    binding.imageButtonEdit
                }) { _, _, _, item ->
                    mainViewModel.currentEntity = item.entity
                    addEditDialogClass.constructors
                        .first().call().show(childFragmentManager)
                }
                addClickListener({ binding: ListItemBinding ->
                    binding.imageButtonDelete
                }) { _, _, _, item ->
                    item.entity.let {
                        mainViewModel.currentEntity = it
                        confirmationMessage = getString(R.string.delete_confirmation, it.title)
                    }
                    confirmationDialog.show(childFragmentManager)
                }
            }

            recyclerView.apply {
                setHasFixedSize(true)
                adapter = fastAdapter
            }

            mainViewModel = provideViewModel(viewModelFactory, mainViewModelClass)
            relatedViewModel = relatedViewModelClass?.let {
                provideViewModel(viewModelFactory, it)
            }

            mainViewModel.comparator?.let { itemListImpl.withComparator(it, true) }

            mainViewModel.listStateFlow
                .filterNotNull()
                .run {
                    if (relatedViewModel != null) {
                        combine(relatedViewModel!!.listStateFlow.filterNotNull()) { main, related ->
                            main to related
                        }.launchRepeatingCollect(viewLifecycleOwner) {
                            handleStates(it.first, it.second, savedInstanceState)
                        }
                    } else {
                        launchRepeatingCollect(viewLifecycleOwner) {
                            handleStates(it, null, savedInstanceState)
                        }
                    }
                }

            mainViewModel.sendingStateFlow.launchRepeatingCollect(viewLifecycleOwner) {
                if (it is SendingState.Success) {
                    progressBar.visibility = View.VISIBLE
                    mainViewModel.getEntities()
                }
            }

            swipeRefreshLayout.apply {
                setOnRefreshListener { mainViewModel.getEntities() }
                setColorSchemeResources(android.R.color.white)
                setProgressBackgroundColorSchemeColor(
                    MaterialColors.getColor(view, R.attr.colorPrimary)
                )
            }

            imageButtonExpandAll.setOnClickListener { toggleAll(true) }
            imageButtonCollapseAll.setOnClickListener { toggleAll(false) }

            val comparator = Comparator<ListItem> { left, right ->
                left.entity.title.compareTo(right.entity.title, true)
            }
            imageButtonSortAsc.setOnClickListener { updateComparator(comparator) }
            imageButtonSortDesc.setOnClickListener { updateComparator(comparator.reversed()) }

            floatingActionButton.setOnClickListener {
                mainViewModel.currentEntity = null
                addEditDialogClass.constructors
                    .first().call().show(parentFragmentManager)
            }
        }
    }

    private fun handleStates(
        mainState: ListState,
        relatedState: ListState?,
        savedInstanceState: Bundle?,
    ) {
        binding.apply {
            when (mainState) {
                is ListState.Loaded -> {
                    if (relatedState != null && relatedState is ListState.Error) {
                        mainViewModel.listStateFlow.value = ListState.Error(R.string.unknown_error)
                        return
                    }

                    textViewMassage.visibility = View.GONE
                    floatingActionButton.visibility = View.VISIBLE

                    FastAdapterDiffUtil[itemAdapter] = mainState.result.data.map { entity ->
                        val relatives = relatedState?.let {
                            if (it is ListState.Loaded) it.result.data
                            else emptyList()
                        }
                        ListItem(entity, relatives)
                    }

                    fastAdapter.withSavedInstanceState(savedInstanceState)
                }
                is ListState.NoItems -> {
                    itemAdapter.clear()
                    textViewMassage.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.list_empty)
                    }
                    floatingActionButton.visibility = View.VISIBLE
                }
                is ListState.Error -> {
                    itemAdapter.clear()
                    textViewMassage.apply {
                        visibility = View.VISIBLE
                        text = getString(mainState.messageStringId)
                    }
                    floatingActionButton.visibility = View.GONE
                }
            }
            progressBar.visibility = View.GONE
            swipeRefreshLayout.apply {
                visibility = View.VISIBLE
                isRefreshing = false
            }
        }
    }

    private fun toggleAll(expand: Boolean) {
        itemAdapter.adapterItems.forEach { it.expanded = expand }
    }

    private fun updateComparator(comparator: Comparator<ListItem>) {
        itemListImpl.withComparator(comparator, true)
        mainViewModel.comparator = comparator
    }

    override fun onConfirm() {
        mainViewModel.run { deleteEntity(currentEntity!!.id) }
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

    override fun onSaveInstanceState(outState: Bundle) {
        mainViewModel.searchQuery = searchView.query.toString()
        super.onSaveInstanceState(fastAdapter.saveInstanceState(outState))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}