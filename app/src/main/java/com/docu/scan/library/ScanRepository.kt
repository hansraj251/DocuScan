package com.docu.scan.library

import android.net.Uri
import kotlinx.coroutines.flow.Flow

class ScanRepository(
    private val dao: ScanDocumentDao,
    private val storage: PdfStorage
) {

    companion object {
        const val DEFAULT_TITLE = "Untitled Scan"
    }

    fun observeAll(): Flow<List<ScanDocument>> {
        return dao.observeAll()
    }

    suspend fun saveScan(
        sourceUri: Uri,
        pageCount: Int,
        timestamp: Long = System.currentTimeMillis()
    ): ScanDocument {

        val pdfFile =
            storage.save(
                sourceUri = sourceUri,
                timestamp = timestamp
            )

        return try {
            val document =
                ScanDocument(
                    title = DEFAULT_TITLE,
                    pdfPath = pdfFile.absolutePath,
                    pageCount = pageCount,
                    createdAt = timestamp
                )

            val id = dao.insert(document)

            document.copy(id = id)
        } catch (exception: Exception) {
            storage.delete(pdfFile.absolutePath)
            throw exception
        }
    }

    suspend fun getById(
        id: Long
    ): ScanDocument? {
        return dao.getById(id)
    }

    suspend fun rename(
        id: Long,
        title: String
    ) {
        val cleanTitle = title.trim()

        require(cleanTitle.isNotEmpty()) {
            "Title cannot be empty"
        }

        dao.rename(
            id = id,
            title = cleanTitle
        )
    }

    suspend fun delete(
        document: ScanDocument
    ) {
        storage.delete(document.pdfPath)
        dao.delete(document)
    }
}
