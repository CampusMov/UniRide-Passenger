package com.campusmov.uniride.domain.filemanagement.usecases

import com.campusmov.uniride.domain.filemanagement.repository.FileManagementRepository

class GetFileUrlUseCase(
    private val repository: FileManagementRepository
) {
    suspend operator fun invoke(folder: String, fileName: String): String =
        repository.getFileUrl(folder, fileName)
}
