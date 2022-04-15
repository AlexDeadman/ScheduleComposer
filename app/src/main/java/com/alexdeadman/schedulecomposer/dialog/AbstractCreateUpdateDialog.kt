package com.alexdeadman.schedulecomposer.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.viewbinding.ViewBinding
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.DialogCreateUpdateBinding
import com.alexdeadman.schedulecomposer.databinding.ThemedButtonBinding
import com.alexdeadman.schedulecomposer.model.entity.Attributes
import com.alexdeadman.schedulecomposer.model.entity.Entity
import com.alexdeadman.schedulecomposer.utils.provideViewModel
import com.alexdeadman.schedulecomposer.viewmodels.AbstractViewModel
import com.alexdeadman.schedulecomposer.viewmodels.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class AbstractCreateUpdateDialog<T : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: DialogCreateUpdateBinding? = null
    protected val binding get() = _binding!!
    protected var fieldsBinding: T? = null

    protected abstract val entityTitleId: Int
    protected abstract val mainViewModelClass: KClass<out AbstractViewModel>
    protected abstract val relatedViewModelClass: KClass<out AbstractViewModel>?

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var mainViewModel: AbstractViewModel
    protected var relatedViewModel: AbstractViewModel? = null

    protected var entity: Entity<out Attributes>? = null
    protected var isNew: Boolean = true

    protected lateinit var layoutParams: RelativeLayout.LayoutParams
    protected val customThemedButton: ThemedButton
        get() = ThemedButtonBinding.inflate(layoutInflater).root.apply {
            applyToTexts {
                it.apply {
                    maxLines = 1
                    ellipsize = TextUtils.TruncateAt.END
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DialogCreateUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = provideViewModel(viewModelFactory, mainViewModelClass)
        relatedViewModel = relatedViewModelClass?.let { provideViewModel(viewModelFactory, it) }

        entity = mainViewModel.currentEntity

        isNew = entity == null || entity?.id == -1

        binding.apply {
            if (isNew) {
                R.string.add_entity to R.string.add
            } else {
                R.string.edit_entity to R.string.edit
            }.let {
                textViewAddEdit.text = getString(it.first, getString(entityTitleId))
                buttonAddEdit.text = getString(it.second)
            }
        }

        layoutParams = resources.run {
            RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                getDimensionPixelSize(R.dimen.themed_button_height)
            ).apply { topMargin = getDimensionPixelSize(R.dimen.themed_button_margin) }
        }

        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isDraggable = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        fieldsBinding = null
    }
}