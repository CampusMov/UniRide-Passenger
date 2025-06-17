package com.campusmov.uniride.domain.filemanagement.usecases

import com.campusmov.uniride.domain.filemanagement.repository.FileManagementRepository

class DeleteFileUseCase(
    private val repository: FileManagementRepository
) {
    suspend operator fun invoke(fileUrl: String): Boolean =
        repository.deleteFile(fileUrl)
}
