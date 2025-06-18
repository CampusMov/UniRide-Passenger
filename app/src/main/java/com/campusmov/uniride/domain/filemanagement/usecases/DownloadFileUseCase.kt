package com.campusmov.uniride.domain.filemanagement.usecases

import com.campusmov.uniride.domain.filemanagement.repository.FileManagementRepository

class DownloadFileUseCase(
    private val repository: FileManagementRepository
) {
    suspend operator fun invoke(fileUrl: String): ByteArray =
        repository.downloadFile(fileUrl)
}
