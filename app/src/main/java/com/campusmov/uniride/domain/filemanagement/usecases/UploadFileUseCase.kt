package com.campusmov.uniride.domain.filemanagement.usecases

import android.net.Uri
import com.campusmov.uniride.domain.filemanagement.repository.FileManagementRepository

class UploadFileUseCase(
    private val repository: FileManagementRepository
) {
    suspend operator fun invoke(uri: Uri, folder: String, fileName: String): String =
        repository.uploadFile(uri, folder, fileName)
}
