package com.example.auddistandroid.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.databinding.FragmentDashboardBinding
import com.example.auddistandroid.utils.ResponseStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: DashboardViewModel by viewModels()

        viewModel.authToken = preferences.getString("authToken", null)!!

        binding.apply {
            smartTable.apply {
                config.apply {
                    isShowTableTitle = false
                    isShowXSequence = false
                    isShowYSequence = false
                }
                setZoom(true, 2f, 0.5f)
            }

            viewModel.schedules.observe(viewLifecycleOwner) {
                if (viewModel.responseStatus == ResponseStatus.SUCCESS) {
                    smartTable.setData(it.data)
                } else {
                    Toast.makeText(
                        context,
                        viewModel.responseStatus.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}