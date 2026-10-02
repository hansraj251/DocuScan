package com.docu.scan.library

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_documents")
data class ScanDocument(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val pdfPath: String,
    val pageCount: Int,
    val createdAt: Long
)
