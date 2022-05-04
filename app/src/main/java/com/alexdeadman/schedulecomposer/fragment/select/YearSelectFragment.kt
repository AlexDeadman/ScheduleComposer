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
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YearSelectFragment : Fragment() {

    private var _binding: FragmentSelectBinding? = null
    private val binding get() = _binding!!

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

            textViewSelect.text = getString(R.string.select_year)
            textViewMassage.visibility = View.GONE
            swipeRefreshLayout.apply {
                isEnabled = false
                visibility = View.VISIBLE
            }

            val syllabuses = requireArguments()
                .getParcelableArray(BundleKeys.SYLLABUS_LIST)!!
                .map { it as Syllabus }

            val itemAdapter = ItemAdapter<SelectItem>()
            val fastAdapter = FastAdapter.with(itemAdapter).apply {
                onClickListener = { _, _, item, _ ->
                    findNavController().navigate(
                        R.id.action_yearSelect_to_semesterSelect,
                        Bundle().apply {
                            putParcelable(
                                BundleKeys.SYLLABUS,
                                syllabuses.single { it.attributes.year == item.title }
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

            FastAdapterDiffUtil[itemAdapter] = syllabuses.map { SelectItem(it.attributes.year) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}