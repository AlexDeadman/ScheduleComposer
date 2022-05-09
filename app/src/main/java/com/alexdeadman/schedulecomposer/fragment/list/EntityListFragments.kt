package com.alexdeadman.schedulecomposer.fragment.list

import com.alexdeadman.schedulecomposer.dialog.addedit.*
import com.alexdeadman.schedulecomposer.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class ClassroomListFragment : AbstractListFragment() {
    override val mainViewModelClass = ClassroomsViewModel::class
    override val relatedViewModelClass: KClass<out AbstractEntityViewModel>? = null
    override val addEditDialogClass = ClassroomDialog::class
}

@AndroidEntryPoint
class DirectionListFragment : AbstractListFragment() {
    override val mainViewModelClass = DirectionsViewModel::class
    override val relatedViewModelClass: KClass<out AbstractEntityViewModel>? = null
    override val addEditDialogClass = DirectionDialog::class
}

@AndroidEntryPoint
class DisciplineListFragment : AbstractListFragment() {
    override val mainViewModelClass = DisciplinesViewModel::class
    override val relatedViewModelClass = SyllabusesViewModel::class
    override val addEditDialogClass = DisciplineDialog::class
}

@AndroidEntryPoint
class GroupListFragment : AbstractListFragment() {
    override val mainViewModelClass = GroupsViewModel::class
    override val relatedViewModelClass = SyllabusesViewModel::class
    override val addEditDialogClass = GroupDialog::class
}

@AndroidEntryPoint
class LecturerListFragment : AbstractListFragment() {
    override val mainViewModelClass = LecturersViewModel::class
    override val relatedViewModelClass: KClass<out AbstractEntityViewModel>? = null
    override val addEditDialogClass = LecturerDialog::class
}

@AndroidEntryPoint
class SyllabusListFragment : AbstractListFragment() {
    override val mainViewModelClass = SyllabusesViewModel::class
    override val relatedViewModelClass = DirectionsViewModel::class
    override val addEditDialogClass = SyllabusDialog::class
}