package com.alexdeadman.schedulecomposer.viewmodel

import com.alexdeadman.schedulecomposer.service.ScApi

class ClassroomsViewModel(api: ScApi) : AbstractEntityViewModel(
    api::getClassrooms,
    api::postClassroom,
    api::putClassroom,
    api::deleteClassroom
)

class DirectionsViewModel(api: ScApi) : AbstractEntityViewModel(
    api::getDirections,
    api::postDirection,
    api::putDirection,
    api::deleteDirection
)

class DisciplinesViewModel(api: ScApi) : AbstractEntityViewModel(
    api::getDisciplines,
    api::postDiscipline,
    api::putDiscipline,
    api::deleteDiscipline
)

class GroupsViewModel(api: ScApi) : AbstractEntityViewModel(
    api::getGroups,
    api::postGroup,
    api::putGroup,
    api::deleteGroup
)

class LecturersViewModel(api: ScApi) : AbstractEntityViewModel(
    api::getLecturers,
    api::postLecturer,
    api::putLecturer,
    api::deleteLecturer
)

class ScheduleViewModel(api: ScApi) : AbstractEntityViewModel(
    api::getSchedule,
    api::postSchedule,
    api::putSchedule,
    api::deleteSchedule
)

class SyllabusesViewModel(api: ScApi) : AbstractEntityViewModel(
    api::getSyllabuses,
    api::postSyllabus,
    api::putSyllabus,
    api::deleteSyllabus
)

