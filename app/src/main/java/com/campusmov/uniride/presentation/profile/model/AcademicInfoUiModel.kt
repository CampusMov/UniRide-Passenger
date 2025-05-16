package com.campusmov.uniride.presentation.profile.model

data class AcademicInfoUiModel(
    val university: String = "",
    val faculty: String = "",
    val academicProgram: String = "",
    val semester: String = "",
    val schedules: List<ClassScheduleUiModel> = emptyList()
)
