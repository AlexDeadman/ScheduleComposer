package com.alexdeadman.auddistandroid.viewmodels

import com.alexdeadman.auddistandroid.service.AudDistApi

class AudiencesViewModel(api: AudDistApi) : AbstractViewModel(api::getAudiences)
class DirectionsViewModel(api: AudDistApi) : AbstractViewModel(api::getDirections)
class DisciplinesViewModel(api: AudDistApi) : AbstractViewModel(api::getDisciplines)
class GroupsViewModel(api: AudDistApi) : AbstractViewModel(api::getGroups)
class LecturersViewModel(api: AudDistApi) : AbstractViewModel(api::getLecturers)
class ScheduleViewModel(api: AudDistApi) : AbstractViewModel(api::getSchedule)
class SyllabusesViewModel(api: AudDistApi) : AbstractViewModel(api::getSyllabuses)