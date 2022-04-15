package com.alexdeadman.schedulecomposer.viewmodels

import com.alexdeadman.schedulecomposer.service.ScApi

class ClassroomsViewModel(api: ScApi) : AbstractViewModel(api::getClassrooms, api::postClassroom, api::putClassroom, api::deleteClassroom)
class DirectionsViewModel(api: ScApi) : AbstractViewModel(api::getDirections, api::postDirection, api::putDirection, api::deleteDirection)
class DisciplinesViewModel(api: ScApi) : AbstractViewModel(api::getDisciplines, api::postDiscipline, api::putDiscipline, api::deleteDiscipline)
class GroupsViewModel(api: ScApi) : AbstractViewModel(api::getGroups, api::postGroup, api::putGroup, api::deleteGroup)
class LecturersViewModel(api: ScApi) : AbstractViewModel(api::getLecturers, api::postLecturer, api::putLecturer, api::deleteLecturer)
class ScheduleViewModel(api: ScApi) : AbstractViewModel(api::getSchedule, api::postSchedule, api::putSchedule, api::deleteSchedule)
class SyllabusesViewModel(api: ScApi) : AbstractViewModel(api::getSyllabuses, api::postSyllabus, api::putSyllabus, api::deleteSyllabus)

