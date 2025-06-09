package com.campusmov.uniride.data.datasource.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.campusmov.uniride.domain.auth.model.Role
import com.campusmov.uniride.domain.auth.model.User
import com.campusmov.uniride.domain.auth.model.UserStatus

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "email")
    var email: String? = "",
    @ColumnInfo(name = "status")
    var status: String? = "",
)

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        email = email ?: "",
        status =  UserStatus.fromString(status ?: UserStatus.NOT_VERIFIED.name),
        roles = listOf(Role.PASSENGER)
    )
}
