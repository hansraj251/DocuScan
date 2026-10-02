package com.docu.scan.library

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScanDocumentTest {

    @Test
    fun newDocument_hasExpectedDefaults() {
        val document = ScanDocument(
            id = 1L,
            title = "Untitled Scan",
            pdfPath = "/scans/test.pdf",
            pageCount = 3,
            createdAt = 1000L
        )

        assertEquals("Untitled Scan", document.title)
        assertEquals(3, document.pageCount)
        assertTrue(document.pdfPath.endsWith(".pdf"))
    }
}
