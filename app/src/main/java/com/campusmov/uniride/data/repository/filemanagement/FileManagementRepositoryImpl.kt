package com.campusmov.uniride.data.repository.filemanagement

import android.net.Uri
import com.campusmov.uniride.domain.filemanagement.repository.FileManagementRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FileManagementRepositoryImpl (
    private val cloudStorage: FirebaseStorage
) : FileManagementRepository {

    override suspend fun uploadFile(uri: Uri, folder: String, fileName: String): String {
        return try {
            val fileRef = cloudStorage.reference.child("$folder/$fileName")
            fileRef.putFile(uri).await()
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            throw FileStorageException("Error uploading file to '$folder/$fileName'", e)
        }
    }

    override suspend fun deleteFile(fileUrl: String): Boolean {
        return try {
            val fileRef = cloudStorage.getReferenceFromUrl(fileUrl)
            fileRef.delete().await()
            true
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun downloadFile(fileUrl: String): ByteArray {
        return try {
            val fileRef = cloudStorage.getReferenceFromUrl(fileUrl)
            fileRef.getBytes(Long.MAX_VALUE).await()
        } catch (e: Exception) {
            throw FileStorageException("Error downloading file from '$fileUrl'", e)
        }
    }

    override suspend fun getFileUrl(folder: String, fileName: String): String {
        return try {
            val fileRef = cloudStorage.reference.child("$folder/$fileName")
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            throw FileStorageException("Error getting file URL for '$folder/$fileName'", e)
        }
    }
}

class FileStorageException(message: String, cause: Throwable? = null) : Exception(message, cause)