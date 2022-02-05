package com.example.auddistandroid.ui.lecturers.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auddistandroid.R
import com.example.auddistandroid.data.model.DataList
import com.example.auddistandroid.data.model.Lecturer
import com.example.auddistandroid.databinding.FragmentLecturersBinding
import com.example.auddistandroid.ui.base.BaseViewModel
import com.example.auddistandroid.utils.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LecturersFragment : Fragment() {

    private var _binding: FragmentLecturersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLecturersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: BaseViewModel by viewModels()

        _binding = FragmentLecturersBinding.bind(view)

        viewModel.state.observe(viewLifecycleOwner) {

            binding.apply {

                when (it) {
                    is State.LoadingState -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is State.LoadedState<*> -> {
                        progressBar.visibility = View.GONE

                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = LecturersAdapter(it.data as DataList<Lecturer>)
                        } // TODO fix cast
                    }
                    is State.NoItemsState -> {
                        progressBar.visibility = View.GONE

                        textViewLecturersError.apply {
                            visibility = View.VISIBLE
                            text = getString(R.string.list_is_empty)
                        }
                    }
                    is State.ErrorState -> {
                        progressBar.visibility = View.GONE

                        textViewLecturersError.apply {
                            visibility = View.VISIBLE
                            text = getString(R.string.unknown_error)
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        viewModel.fetchEntities()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}