package com.alexdeadman.schedulecomposer.fragment.list

import com.alexdeadman.schedulecomposer.dialog.addedit.*
import com.alexdeadman.schedulecomposer.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassroomListFragment : AbstractListFragment() {
    override val mainViewModelClass = ClassroomsViewModel::class
    override val relatedViewModelClass = null
    override val addEditDialog = ClassroomDialog()
}

@AndroidEntryPoint
class DirectionListFragment : AbstractListFragment() {
    override val mainViewModelClass = DirectionsViewModel::class
    override val relatedViewModelClass = null
    override val addEditDialog = DirectionDialog()
}

@AndroidEntryPoint
class DisciplineListFragment : AbstractListFragment() {
    override val mainViewModelClass = DisciplinesViewModel::class
    override val relatedViewModelClass = SyllabusesViewModel::class
    override val addEditDialog = DisciplineDialog()
}

@AndroidEntryPoint
class GroupListFragment : AbstractListFragment() {
    override val mainViewModelClass = GroupsViewModel::class
    override val relatedViewModelClass = SyllabusesViewModel::class
    override val addEditDialog = GroupDialog()
}

@AndroidEntryPoint
class LecturerListFragment : AbstractListFragment() {
    override val mainViewModelClass = LecturersViewModel::class
    override val relatedViewModelClass = DisciplinesViewModel::class
    override val addEditDialog = LecturerDialog()
}

@AndroidEntryPoint
class SyllabusListFragment : AbstractListFragment() {
    override val mainViewModelClass = SyllabusesViewModel::class
    override val relatedViewModelClass = DirectionsViewModel::class
    override val addEditDialog = SyllabusDialog()
}