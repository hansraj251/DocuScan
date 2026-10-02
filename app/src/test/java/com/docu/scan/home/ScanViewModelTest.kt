package com.docu.scan.home

import com.docu.scan.library.ScanDocument
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ScanViewModelTest {

    @Test
    fun documents_areSortedNewestFirst() {
        val older =
            ScanDocument(
                id = 1L,
                title = "Older",
                pdfPath = "/scans/older.pdf",
                pageCount = 1,
                createdAt = 100L
            )

        val newer =
            ScanDocument(
                id = 2L,
                title = "Newer",
                pdfPath = "/scans/newer.pdf",
                pageCount = 2,
                createdAt = 200L
            )

        val result =
            ScanViewModel.sortDocuments(
                listOf(older, newer)
            )

        assertEquals(2L, result.first().id)
        assertEquals(1L, result.last().id)
    }

    @Test
    fun emptyDocuments_areDetected() {
        assertFalse(
            ScanViewModel.hasDocuments(emptyList())
        )

        assertEquals(
            true,
            ScanViewModel.hasDocuments(
                listOf(
                    ScanDocument(
                        id = 1L,
                        title = "Scan",
                        pdfPath = "/scans/test.pdf",
                        pageCount = 1,
                        createdAt = 100L
                    )
                )
            )
        )
    }
}
