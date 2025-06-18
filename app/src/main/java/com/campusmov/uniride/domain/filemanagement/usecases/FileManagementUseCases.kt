package com.campusmov.uniride.domain.filemanagement.usecases

data class FileManagementUseCases(
    val uploadFileUseCase: UploadFileUseCase,
    val deleteFileUseCase: DeleteFileUseCase,
    val getFileUrlUseCase: GetFileUrlUseCase,
    val downloadFileUseCase: DownloadFileUseCase
)