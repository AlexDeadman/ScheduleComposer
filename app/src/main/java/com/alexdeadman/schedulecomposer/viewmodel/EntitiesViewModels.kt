package com.alexdeadman.schedulecomposer.viewmodel

import com.alexdeadman.schedulecomposer.service.Api
import com.google.gson.Gson

class ClassroomsViewModel(gson: Gson, api: Api) : AbstractEntityViewModel(
    gson,
    api::getClassrooms,
    api::postClassroom,
    api::putClassroom,
    api::deleteClassroom
)

class DirectionsViewModel(gson: Gson, api: Api) : AbstractEntityViewModel(
    gson,
    api::getDirections,
    api::postDirection,
    api::putDirection,
    api::deleteDirection
)

class DisciplinesViewModel(gson: Gson, api: Api) : AbstractEntityViewModel(
    gson,
    api::getDisciplines,
    api::postDiscipline,
    api::putDiscipline,
    api::deleteDiscipline
)

class GroupsViewModel(gson: Gson, api: Api) : AbstractEntityViewModel(
    gson,
    api::getGroups,
    api::postGroup,
    api::putGroup,
    api::deleteGroup
)

class LecturersViewModel(gson: Gson, api: Api) : AbstractEntityViewModel(
    gson,
    api::getLecturers,
    api::postLecturer,
    api::putLecturer,
    api::deleteLecturer
)

class ScheduleViewModel(gson: Gson, api: Api) : AbstractEntityViewModel(
    gson,
    api::getSchedule,
    api::postSchedule,
    api::putSchedule,
    api::deleteSchedule
)

class SyllabusesViewModel(gson: Gson, api: Api) : AbstractEntityViewModel(
    gson,
    api::getSyllabuses,
    api::postSyllabus,
    api::putSyllabus,
    api::deleteSyllabus
)

