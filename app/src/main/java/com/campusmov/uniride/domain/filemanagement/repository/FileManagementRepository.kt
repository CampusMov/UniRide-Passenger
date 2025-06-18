package com.campusmov.uniride.domain.filemanagement.repository

import android.net.Uri

interface FileManagementRepository {
    suspend fun uploadFile(uri: Uri, folder: String, fileName: String): String

    suspend fun deleteFile(fileUrl: String): Boolean

    suspend fun downloadFile(fileUrl: String): ByteArray

    suspend fun getFileUrl(folder: String, fileName: String): String
}