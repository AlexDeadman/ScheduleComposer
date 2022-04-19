package com.alexdeadman.schedulecomposer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.adapter.ScheduleItem
import com.alexdeadman.schedulecomposer.databinding.FragmentScheduleBinding
import com.alexdeadman.schedulecomposer.model.entity.*
import com.alexdeadman.schedulecomposer.util.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.util.provideViewModel
import com.alexdeadman.schedulecomposer.util.state.ListState.*
import com.alexdeadman.schedulecomposer.viewmodel.*
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            smartTable.apply {
                config.apply {
                    isShowTableTitle = false
                    isShowXSequence = false
                    isShowYSequence = false
                }
                setZoom(true, 1.25f, 0.30f)
            }

            val viewModels = listOf(
                ScheduleViewModel::class,
                LecturersViewModel::class,
                DisciplinesViewModel::class,
                GroupsViewModel::class,
                ClassroomsViewModel::class
            ).map {
                provideViewModel(viewModelFactory, it)
            }

            combine(
                *viewModels.map { it.state.filterNotNull() }.toTypedArray()
            ) { stateArray -> stateArray.toList() }
                .launchRepeatingCollect(viewLifecycleOwner) { stateList ->
                    when (val scheduleState = stateList.first()) {
                        is Loaded -> {
                            if (stateList.any { it !is Loaded }) {
                                viewModels.first().state.value = Error(R.string.unknown_error)
                            }
                            textViewMassage.visibility = View.GONE

                            val results = stateList.map { (it as Loaded).result.data }
                            smartTable.setData(
                                results[0].map {
                                    ScheduleItem(
                                        requireContext(),
                                        it as Schedule,
                                        results[1] as List<Lecturer>,
                                        results[2] as List<Discipline>,
                                        results[3] as List<Group>,
                                        results[4] as List<Classroom>,
                                    )
                                }.sortedWith(
                                    compareBy(
                                        ScheduleItem::semester,
                                        ScheduleItem::group,
                                        ScheduleItem::evenWeek,
                                        ScheduleItem::weekDay,
                                        ScheduleItem::period
                                    )
                                )
                            )
                        }
                        is NoItems -> {
                            textViewMassage.apply {
                                visibility = View.VISIBLE
                                text = getString(R.string.table_empty)
                            }
                        }
                        is Error -> {
                            textViewMassage.apply {
                                visibility = View.VISIBLE
                                text = getString(scheduleState.messageStringId)
                            }
                        }
                    }
                    progressBar.visibility = View.GONE
                    swipeRefreshLayout.apply {
                        visibility = View.VISIBLE
                        isRefreshing = false
                    }
                }

            swipeRefreshLayout.apply {
                setOnRefreshListener {
                    viewModels.first().getEntities()
                }
                setColorSchemeResources(android.R.color.white)
                setProgressBackgroundColorSchemeColor(
                    MaterialColors.getColor(view, R.attr.colorPrimary)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}