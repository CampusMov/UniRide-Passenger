package com.campusmov.uniride.domain.auth.usecases

data class UserUseCase (
    val saveUserLocallyUseCase: SaveUserLocallyUseCase,
    val getUserByIdLocallyUserCase: GetUserByIdLocallyUserCase,
    val getUserByEmailLocallyUseCase: GetUserByEmailLocallyUseCase,
    val updateUserLocallyUseCase: UpdateUserLocallyUseCase,
    val getUserLocallyUseCase: GetUserLocallyUseCase,
    val deleteAllUsersLocallyUseCase: DeleteAllUsersLocallyUseCase,
)