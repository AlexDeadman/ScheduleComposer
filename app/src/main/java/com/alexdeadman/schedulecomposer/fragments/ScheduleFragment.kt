package com.alexdeadman.schedulecomposer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexdeadman.schedulecomposer.databinding.FragmentScheduleBinding
import com.alexdeadman.schedulecomposer.utils.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.utils.provideViewModel
import com.alexdeadman.schedulecomposer.utils.state.ListState.*
import com.alexdeadman.schedulecomposer.viewmodels.ScheduleViewModel
import com.alexdeadman.schedulecomposer.viewmodels.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
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
        savedInstanceState: Bundle?
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
                setZoom(true, 2f, 0.5f)
            }

            val viewModel = provideViewModel(viewModelFactory, ScheduleViewModel::class)

            viewModel.fetchState
                .filterNotNull()
                .launchRepeatingCollect(viewLifecycleOwner) { state ->
                    when (state) { // TODO
                        is Loaded -> smartTable.setData(state.result.data)
                        is NoItems -> { }
                        is Error -> { }
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}