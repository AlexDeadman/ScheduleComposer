package com.alexdeadman.schedulecomposer.viewmodels

import com.alexdeadman.schedulecomposer.service.AudDistApi

class AudiencesViewModel(api: AudDistApi) : AbstractViewModel(api::getClassrooms)
class DirectionsViewModel(api: AudDistApi) : AbstractViewModel(api::getDirections)
class DisciplinesViewModel(api: AudDistApi) : AbstractViewModel(api::getDisciplines)
class GroupsViewModel(api: AudDistApi) : AbstractViewModel(api::getGroups)
class LecturersViewModel(api: AudDistApi) : AbstractViewModel(api::getLecturers)
class ScheduleViewModel(api: AudDistApi) : AbstractViewModel(api::getSchedule)
class SyllabusesViewModel(api: AudDistApi) : AbstractViewModel(api::getSyllabuses)