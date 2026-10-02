package com.docu.scan.library

import android.content.Context
import android.net.Uri
import java.io.File

class PdfStorage(
    private val context: Context
) {

    companion object {
        const val SCAN_DIRECTORY = "scans"
    }

    private val directory: File
        get() = File(
            context.filesDir,
            SCAN_DIRECTORY
        ).apply {
            mkdirs()
        }

    fun save(
        sourceUri: Uri,
        timestamp: Long
    ): File {

        val destination =
            File(
                directory,
                ScanFileName.pdf(timestamp)
            )

        context.contentResolver.openInputStream(sourceUri).use { input ->
            requireNotNull(input) {
                "Unable to open PDF source"
            }

            destination.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return destination
    }

    fun delete(
        filePath: String
    ): Boolean {
        return File(filePath).delete()
    }
}
