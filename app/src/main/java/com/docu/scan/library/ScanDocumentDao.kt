package com.docu.scan.library

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDocumentDao {

    @Insert
    suspend fun insert(document: ScanDocument): Long

    @Query(
        """
        SELECT *
        FROM scan_documents
        ORDER BY createdAt DESC
        """
    )
    fun observeAll(): Flow<List<ScanDocument>>

    @Query(
        """
        SELECT *
        FROM scan_documents
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun getById(id: Long): ScanDocument?

    @Query(
        """
        UPDATE scan_documents
        SET title = :title
        WHERE id = :id
        """
    )
    suspend fun rename(
        id: Long,
        title: String
    )

    @Delete
    suspend fun delete(document: ScanDocument)
}
