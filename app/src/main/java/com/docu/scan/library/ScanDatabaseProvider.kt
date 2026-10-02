package com.docu.scan.library

import android.content.Context
import androidx.room.Room

object ScanDatabaseProvider {

    const val DATABASE_NAME = "docuscan.db"

    @Volatile
    private var instance: ScanDatabase? = null

    fun get(
        context: Context
    ): ScanDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room
                .databaseBuilder(
                    context.applicationContext,
                    ScanDatabase::class.java,
                    DATABASE_NAME
                )
                .build()
                .also {
                    instance = it
                }
        }
    }
}
