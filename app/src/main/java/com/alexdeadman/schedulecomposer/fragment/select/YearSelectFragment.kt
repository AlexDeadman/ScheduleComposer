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
import com.alexdeadman.schedulecomposer.util.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.util.provideViewModel
import com.alexdeadman.schedulecomposer.util.state.ListState
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
class YearSelectFragment : Fragment() {

    private var _binding: FragmentSelectBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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

            textViewSelect.text = getString(R.string.select_year)

            val itemAdapter = ItemAdapter<SelectItem>()
            val fastAdapter = FastAdapter.with(itemAdapter).apply {
                onClickListener = { _, _, item, _ ->
                    findNavController().navigate(
                        R.id.action_yearSelect_to_semesterSelect,
                        Bundle().apply { putInt("syllabus_id", item.id!!) }
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

                    textViewMassage.visibility = View.GONE

                    FastAdapterDiffUtil[itemAdapter] =
                        (state as ListState.Loaded).result.data
                            .map { it as Syllabus }
                            .filter {
                                it.attributes.name == requireArguments().getString("syllabus_name")
                            }
                            .map { SelectItem(it.attributes.year, it.id) }

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