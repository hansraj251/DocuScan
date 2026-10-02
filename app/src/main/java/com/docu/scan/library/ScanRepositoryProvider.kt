package com.docu.scan.library

import android.content.Context

object ScanRepositoryProvider {

    const val DATABASE_NAME =
        ScanDatabaseProvider.DATABASE_NAME

    @Volatile
    private var instance: ScanRepository? = null

    fun get(
        context: Context
    ): ScanRepository {
        return instance ?: synchronized(this) {

            instance ?: run {
                val database =
                    ScanDatabaseProvider.get(context)

                val storage =
                    PdfStorage(context.applicationContext)

                ScanRepository(
                    dao = database.scanDocumentDao(),
                    storage = storage
                ).also {
                    instance = it
                }
            }
        }
    }
}
