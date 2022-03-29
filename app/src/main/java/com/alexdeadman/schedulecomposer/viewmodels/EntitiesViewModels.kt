package com.alexdeadman.schedulecomposer.viewmodels

import com.alexdeadman.schedulecomposer.service.ScApi

class ClassroomsViewModel(api: ScApi) : AbstractViewModel(api::getClassrooms)
class DirectionsViewModel(api: ScApi) : AbstractViewModel(api::getDirections)
class DisciplinesViewModel(api: ScApi) : AbstractViewModel(api::getDisciplines)
class GroupsViewModel(api: ScApi) : AbstractViewModel(api::getGroups)
class LecturersViewModel(api: ScApi) : AbstractViewModel(api::getLecturers)
class ScheduleViewModel(api: ScApi) : AbstractViewModel(api::getSchedule)
class SyllabusesViewModel(api: ScApi) : AbstractViewModel(api::getSyllabuses)