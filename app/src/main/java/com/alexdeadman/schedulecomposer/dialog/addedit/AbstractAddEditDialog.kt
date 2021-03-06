package com.alexdeadman.schedulecomposer.dialog.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.DialogAddEditBinding
import com.alexdeadman.schedulecomposer.model.entity.Attributes
import com.alexdeadman.schedulecomposer.model.entity.Entity
import com.alexdeadman.schedulecomposer.util.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.util.provideViewModel
import com.alexdeadman.schedulecomposer.util.state.SendingState
import com.alexdeadman.schedulecomposer.viewmodel.AbstractEntityViewModel
import com.alexdeadman.schedulecomposer.viewmodel.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class AbstractAddEditDialog<T : ViewBinding> : BottomSheetDialogFragment() {

    protected abstract val mainViewModelClass: KClass<out AbstractEntityViewModel>
    protected abstract val relatedViewModelClass: KClass<out AbstractEntityViewModel>?
    protected abstract val entityTitleId: Int

    private var _binding: DialogAddEditBinding? = null
    protected val binding get() = _binding!!

    private var _fieldsBinding: T? = null
    protected val fieldsBinding get() = _fieldsBinding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var mainViewModel: AbstractEntityViewModel
    protected var relatedViewModel: AbstractEntityViewModel? = null

    protected var currentEntity: Entity<out Attributes>? = null

    protected var freezingViews: List<View>? = null

    protected abstract fun createBinding(inflater: LayoutInflater): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DialogAddEditBinding.inflate(inflater, container, false)

        _fieldsBinding = createBinding(inflater)
        binding.frameLayoutFields.addView(fieldsBinding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = provideViewModel(viewModelFactory, mainViewModelClass)
        relatedViewModel = relatedViewModelClass?.let { provideViewModel(viewModelFactory, it) }

        currentEntity = mainViewModel.currentEntity

        binding.apply {
            if (currentEntity == null || currentEntity?.id == -1) {
                R.string.add_entity to R.string.add
            } else {
                R.string.edit_entity to R.string.edit
            }.let {
                textViewAddEdit.text = getString(it.first, getString(entityTitleId))
                buttonAddEdit.text = getString(it.second)
            }

            mainViewModel.sendingStateFlow.apply {
                value = SendingState.Default()
                launchRepeatingCollect(viewLifecycleOwner) { state ->
                    when (state) {
                        is SendingState.Default -> {}
                        is SendingState.Sending -> {
                            isCancelable = false
                            buttonAddEdit.isEnabled = false
                            freezingViews!!.forEach { it.isEnabled = false }
                            progressBar.visibility = View.VISIBLE
                            textViewError.visibility = View.INVISIBLE
                        }
                        is SendingState.Success -> dismiss()
                        is SendingState.Error -> {
                            isCancelable = true
                            buttonAddEdit.isEnabled = true
                            freezingViews!!.forEach { it.isEnabled = true }
                            progressBar.visibility = View.INVISIBLE
                            textViewError.apply {
                                text = getString(state.messageStringId)
                                visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }

        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isDraggable = false
        }
    }

    protected fun send(entity: Entity<out Attributes>) {
        mainViewModel.run {
            if (currentEntity == null) postEntity(entity)
            else {
                if (currentEntity != entity) putEntity(entity)
                else dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fieldsBinding = null
        _binding = null
    }
}