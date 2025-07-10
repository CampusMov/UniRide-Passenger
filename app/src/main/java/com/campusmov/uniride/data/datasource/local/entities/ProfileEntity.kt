package com.campusmov.uniride.data.datasource.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.campusmov.uniride.data.datasource.local.assembler.ClassScheduleConverter
import com.campusmov.uniride.domain.profile.model.EGender
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.profile.model.ClassSchedule

@Entity(tableName = "profiles")
@TypeConverters(ClassScheduleConverter::class)
data class ProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "userId")
    val userId: String,
    @ColumnInfo(name = "firstName")
    val firstName: String?,
    @ColumnInfo(name = "lastName")
    val lastName: String?,
    @ColumnInfo(name = "profilePictureUrl")
    val profilePictureUrl: String?,
    @ColumnInfo(name = "birthDate")
    val birthDate: String?,
    @ColumnInfo(name = "gender")
    val gender: String?,
    @ColumnInfo(name = "institutionalEmailAddress")
    val institutionalEmailAddress: String?,
    @ColumnInfo(name = "personalEmailAddress")
    val personalEmailAddress: String?,
    @ColumnInfo(name = "countryCode")
    val countryCode: String?,
    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String?,
    @ColumnInfo(name = "university")
    val university: String?,
    @ColumnInfo(name = "faculty")
    val faculty: String?,
    @ColumnInfo(name = "academicProgram")
    val academicProgram: String?,
    @ColumnInfo(name = "semester")
    val semester: String?,
    @ColumnInfo(name = "classSchedules")
    val classSchedules: List<ClassSchedule>?,
    @ColumnInfo(name = "localChanges")
    val localChanges: Int?
)

fun ProfileEntity.toDomain(): Profile {
    return Profile(
        userId = userId,
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        profilePictureUrl = profilePictureUrl ?: "",
        birthDate = birthDate,
        gender = EGender.fromString(gender),
        institutionalEmailAddress = institutionalEmailAddress ?: "",
        personalEmailAddress = personalEmailAddress ?: "",
        countryCode = countryCode ?: "+51",
        phoneNumber = phoneNumber ?: "",
        university = university ?: "",
        faculty = faculty ?: "",
        academicProgram = academicProgram ?: "",
        semester = semester ?: "",
        classSchedules = classSchedules ?: emptyList()
    )
}

fun Profile.toEntity(): ProfileEntity {
    return ProfileEntity(
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        profilePictureUrl = profilePictureUrl,
        birthDate = birthDate,
        gender = gender.name,
        institutionalEmailAddress = institutionalEmailAddress,
        personalEmailAddress = personalEmailAddress,
        countryCode = countryCode,
        phoneNumber = phoneNumber,
        university = university,
        faculty = faculty,
        academicProgram = academicProgram,
        semester = semester,
        classSchedules = classSchedules,
        localChanges = 0
    )
}