package com.docu.scan.library

import org.junit.Assert.assertTrue
import org.junit.Test

class ScanFileNameTest {

    @Test
    fun generatedPdfName_hasPdfExtension() {
        val name = ScanFileName.pdf(12345L)

        assertTrue(name.endsWith(".pdf"))
        assertTrue(name.startsWith("scan_"))
    }
}
