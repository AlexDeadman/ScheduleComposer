package com.alexdeadman.schedulecomposer.fragment.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.adapter.SelectItem
import com.alexdeadman.schedulecomposer.databinding.FragmentSelectBinding
import com.alexdeadman.schedulecomposer.model.entity.Syllabus
import com.alexdeadman.schedulecomposer.util.key.BundleKeys
import com.alexdeadman.schedulecomposer.util.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.util.provideViewModel
import com.alexdeadman.schedulecomposer.util.state.ListState.*
import com.alexdeadman.schedulecomposer.viewmodel.SyllabusesViewModel
import com.alexdeadman.schedulecomposer.viewmodel.ViewModelFactory
import com.google.android.material.color.MaterialColors
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@AndroidEntryPoint
class SyllabusSelectFragment : Fragment() {

    private var _binding: FragmentSelectBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var syllabuses: List<Syllabus>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            swipeRefreshLayout.apply {
                setColorSchemeResources(android.R.color.white)
                setProgressBackgroundColorSchemeColor(
                    MaterialColors.getColor(view, R.attr.colorPrimary)
                )
            }

            textViewSelect.text = getString(R.string.select_syllabus)

            val itemAdapter = ItemAdapter<SelectItem>()
            val fastAdapter = FastAdapter.with(itemAdapter).apply {
                onClickListener = { _, _, item, _ ->
                    findNavController().navigate(
                        R.id.action_syllabusSelect_to_yearSelect,
                        Bundle().apply {
                            putParcelableArray(
                                BundleKeys.SYLLABUS_LIST,
                                syllabuses.filter {
                                    it.attributes.name == item.title
                                }.toTypedArray()
                            )
                        }
                    )
                    false
                }
            }

            recyclerView.apply {
                setHasFixedSize(true)
                adapter = fastAdapter
            }

            val viewModel = provideViewModel(viewModelFactory, SyllabusesViewModel::class)

            viewModel.state
                .filterNotNull()
                .launchRepeatingCollect(viewLifecycleOwner) { state ->
                    when (state) {
                        is Loaded -> {
                            textViewMassage.visibility = View.GONE

                            syllabuses = state.result.data.map { it as Syllabus }

                            FastAdapterDiffUtil[itemAdapter] = syllabuses
                                .map { it.attributes.name }
                                .distinct()
                                .map { SelectItem(it, R.drawable.ic_syllabus) }
                        }
                        is NoItems -> {
                            itemAdapter.clear()
                            textViewMassage.apply {
                                visibility = View.VISIBLE
                                text = getString(R.string.list_empty)
                            }
                        }
                        is Error -> {
                            itemAdapter.clear()
                            textViewMassage.apply {
                                visibility = View.VISIBLE
                                text = getString(state.messageStringId)
                            }
                        }
                    }
                    progressBar.visibility = View.GONE
                    swipeRefreshLayout.apply {
                        visibility = View.VISIBLE
                        isRefreshing = false
                    }
                }

            swipeRefreshLayout.setOnRefreshListener { viewModel.getEntities() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}